-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 08, 2025 at 09:31 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `room.db`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `BookRoom` (IN `p_room_id` INT, IN `p_customer_id` INT, IN `p_checkin_date` DATE, IN `p_checkout_date` DATE)   BEGIN
    DECLARE room_price DECIMAL(10, 2);
    DECLARE days INT;
    DECLARE total DECIMAL(10, 2);
    
    -- Calculate total price
    SELECT rt.price_per_night INTO room_price
    FROM Rooms r 
    JOIN RoomTypes rt ON r.type_id = rt.type_id
    WHERE r.room_id = p_room_id;
    
    SET days = DATEDIFF(p_checkout_date, p_checkin_date);
    SET total = room_price * days;
    
    -- Create the booking
    INSERT INTO Bookings (room_id, customer_id, checkin_date, checkout_date, total_price)
    VALUES (p_room_id, p_customer_id, p_checkin_date, p_checkout_date, total);
    
    -- Update room availability
    UPDATE Rooms SET is_available = FALSE WHERE room_id = p_room_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckIn` (IN `p_booking_id` INT)   BEGIN
    UPDATE Bookings SET status = 'Checked-in' WHERE booking_id = p_booking_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `CheckOut` (IN `p_booking_id` INT)   BEGIN
    DECLARE room_id_val INT;
    
    -- Get room_id from booking
    SELECT room_id INTO room_id_val FROM Bookings WHERE booking_id = p_booking_id;
    
    -- Update booking status
    UPDATE Bookings SET status = 'Checked-out' WHERE booking_id = p_booking_id;
    
    -- Make room available again
    UPDATE Rooms SET is_available = TRUE WHERE room_id = room_id_val;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SearchAvailableRooms` (IN `p_checkin_date` DATE, IN `p_checkout_date` DATE, IN `p_capacity` INT)   BEGIN
    SELECT r.room_id, r.room_number, rt.type_name, rt.price_per_night, r.capacity
    FROM Rooms r
    JOIN RoomTypes rt ON r.type_id = rt.type_id
    WHERE r.is_available = TRUE
    AND r.capacity >= p_capacity
    AND r.room_id NOT IN (
        SELECT b.room_id 
        FROM Bookings b
        WHERE (p_checkin_date BETWEEN b.checkin_date AND DATE_SUB(b.checkout_date, INTERVAL 1 DAY))
        OR (p_checkout_date BETWEEN DATE_ADD(b.checkin_date, INTERVAL 1 DAY) AND b.checkout_date)
        OR (b.checkin_date BETWEEN p_checkin_date AND DATE_SUB(p_checkout_date, INTERVAL 1 DAY))
        OR (b.checkout_date BETWEEN DATE_ADD(p_checkin_date, INTERVAL 1 DAY) AND p_checkout_date)
    );
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `ID` int(11) NOT NULL,
  `Room_ID` int(11) DEFAULT NULL,
  `Customer_ID` int(11) DEFAULT NULL,
  `Checkin_Date` date NOT NULL,
  `Checkout_Date` date NOT NULL,
  `Status` varchar(20) DEFAULT 'Booked',
  `Total_Price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`ID`, `Room_ID`, `Customer_ID`, `Checkin_Date`, `Checkout_Date`, `Status`, `Total_Price`) VALUES
(1, 1, 2, '2025-05-01', '2025-05-05', 'Booked', 200.00),
(2, 2, 1, '2025-05-03', '2025-05-07', 'Booked', 300.00),
(3, 4, 4, '2025-05-10', '2025-05-12', 'Booked', 150.00),
(4, 5, 6, '2025-05-15', '2025-05-20', 'Booked', 500.00),
(5, 6, 3, '2025-05-08', '2025-05-10', 'Booked', 180.00),
(6, 8, 8, '2025-05-18', '2025-05-22', 'Booked', 400.00),
(7, 10, 9, '2025-05-11', '2025-05-15', 'Booked', 250.00),
(8, 3, 5, '2025-05-19', '2025-05-25', 'Booked', 600.00),
(9, 7, 7, '2025-05-20', '2025-05-23', 'Booked', 180.00),
(10, 9, 10, '2025-05-05', '2025-05-10', 'Booked', 300.00);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `ID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Address` text DEFAULT NULL,
  `Registration_Date` date NOT NULL DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`ID`, `Name`, `Phone`, `Email`, `Address`, `Registration_Date`) VALUES
(1, 'John Smith', '555-1234', 'john.smith@example.com', NULL, '2025-05-08'),
(2, 'Alice Johnson', '555-5678', 'alice.johnson@example.com', NULL, '2025-05-08'),
(3, 'Bob Williams', '555-8765', 'bob.williams@example.com', NULL, '2025-05-08'),
(4, 'Emily Davis', '555-4321', 'emily.davis@example.com', NULL, '2025-05-08'),
(5, 'Michael Brown', '555-2468', 'michael.brown@example.com', NULL, '2025-05-08'),
(6, 'Sarah Wilson', '555-1357', 'sarah.wilson@example.com', NULL, '2025-05-08'),
(7, 'David Lee', '555-9876', 'david.lee@example.com', NULL, '2025-05-08'),
(8, 'Laura Martinez', '555-6543', 'laura.martinez@example.com', NULL, '2025-05-08'),
(9, 'Kevin Garcia', '555-7890', 'kevin.garcia@example.com', NULL, '2025-05-08'),
(10, 'Olivia Rodriguez', '555-3210', 'olivia.rodriguez@example.com', NULL, '2025-05-08');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `ID` int(11) NOT NULL,
  `Number` varchar(10) NOT NULL,
  `Type` varchar(50) NOT NULL,
  `Availability` tinyint(1) NOT NULL DEFAULT 1,
  `Price` decimal(10,2) NOT NULL,
  `Description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`ID`, `Number`, `Type`, `Availability`, `Price`, `Description`) VALUES
(1, '101', 'Single', 1, 100.00, 'Single room with one bed'),
(2, '102', 'Double', 1, 150.00, 'Double room with two beds'),
(3, '103', 'Suite', 0, 250.00, 'Luxury suite with balcony'),
(4, '104', 'Single', 1, 100.00, 'Single room with one bed'),
(5, '105', 'Double', 1, 150.00, 'Double room with two beds'),
(6, '106', 'Suite', 1, 250.00, 'Luxury suite with living area'),
(7, '107', 'Single', 0, 100.00, 'Single room under maintenance'),
(8, '108', 'Double', 1, 150.00, 'Double room with two beds'),
(9, '109', 'Suite', 1, 250.00, 'Luxury suite with ocean view'),
(10, '110', 'Single', 1, 100.00, 'Single room with quiet atmosphere');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Room_ID` (`Room_ID`),
  ADD KEY `Customer_ID` (`Customer_ID`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`Room_ID`) REFERENCES `rooms` (`ID`),
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`Customer_ID`) REFERENCES `customers` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
