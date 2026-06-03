-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: db_deuce
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `court_id` bigint unsigned NOT NULL,
  `booking_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `duration_hours` decimal(4,2) NOT NULL,
  `total_price` decimal(12,2) NOT NULL,
  `status` enum('pending','waiting_payment','confirmed','rejected','cancelled','done') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `notes` text COLLATE utf8mb4_unicode_ci,
  `qr_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `qr_token` (`qr_token`),
  KEY `idx_booking_slot` (`court_id`,`booking_date`,`start_time`,`end_time`),
  KEY `idx_booking_user` (`user_id`,`status`),
  CONSTRAINT `fk_b_court` FOREIGN KEY (`court_id`) REFERENCES `courts` (`id`),
  CONSTRAINT `fk_b_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (2,2,1,'2026-06-02','12:00:00','13:00:00',1.00,150000.00,'confirmed',NULL,'4d1afa40-79ac-416e-b5a0-5ab01587e722','2026-05-31 11:13:57','2026-06-03 10:26:35'),(3,2,2,'2026-06-03','08:00:00','09:00:00',1.00,120000.00,'pending',NULL,'c3be5b69-fe16-497b-914c-83a00c171b1c','2026-05-31 11:13:57','2026-05-31 11:13:57'),(4,2,1,'2026-06-02','09:00:00','10:00:00',1.00,150000.00,'rejected',NULL,'0065a43f-5af5-4d06-8abe-f217a02a0414','2026-05-31 11:39:36','2026-06-03 10:18:06'),(5,4,1,'2026-06-06','08:00:00','09:00:00',1.00,150000.00,'rejected',NULL,'40cf606a-8511-4659-bf11-0eda13d1fcf8','2026-06-02 14:12:47','2026-06-03 10:18:14'),(6,4,1,'2026-06-05','16:00:00','18:00:00',2.00,300000.00,'confirmed',NULL,'4c1132e7-10f3-40be-800e-e66f3c652291','2026-06-02 14:32:48','2026-06-02 14:35:56'),(7,4,1,'2026-06-05','19:00:00','20:00:00',1.00,150000.00,'rejected','','daaaca6a-e9ad-43ba-b5e7-f4375e413ca5','2026-06-03 09:04:53','2026-06-03 10:18:14'),(8,4,1,'2026-06-05','14:00:00','15:00:00',1.00,150000.00,'pending','','565ac835-0605-491e-b6c7-c2d26ed6344c','2026-06-03 09:05:46','2026-06-03 09:05:46'),(9,4,1,'2026-06-04','08:00:00','09:00:00',1.00,150000.00,'waiting_payment','','843d636b-4af0-492f-9a98-42ecef7779ff','2026-06-03 09:12:24','2026-06-03 09:12:33'),(10,4,1,'2026-06-04','10:00:00','11:00:00',1.00,150000.00,'waiting_payment',NULL,'f039ea2b-61b9-430a-8ce3-1f36455397ca','2026-06-03 09:19:47','2026-06-03 09:20:20'),(11,4,1,'2026-06-04','09:00:00','10:00:00',1.00,150000.00,'waiting_payment',NULL,'690fa1a8-aa7c-4fef-a762-7e777c776f93','2026-06-03 09:21:06','2026-06-03 09:21:14'),(12,4,1,'2026-06-04','11:00:00','12:00:00',1.00,150000.00,'waiting_payment','DJ jj','9f2b8255-aa88-4a5d-bb47-775ecb596800','2026-06-03 09:26:34','2026-06-03 09:26:38'),(13,4,1,'2026-06-04','07:00:00','08:00:00',1.00,150000.00,'waiting_payment','','ecf379ff-46e2-4d3c-9417-f38b2b23ddff','2026-06-03 09:27:03','2026-06-03 09:27:13'),(14,4,2,'2026-06-04','09:00:00','10:00:00',1.00,120000.00,'waiting_payment','','bd283f3a-ca32-45fc-8632-07bef4f11a52','2026-06-03 09:27:58','2026-06-03 09:28:06'),(15,4,3,'2026-06-05','08:00:00','09:00:00',1.00,200000.00,'confirmed','','fa346774-9e94-4ecf-a361-e395c0dcd5e1','2026-06-03 10:06:42','2026-06-03 10:08:35'),(16,4,1,'2026-06-12','08:00:00','09:00:00',1.00,150000.00,'confirmed','','06604a6d-0730-4b74-85ad-717fb5ba0078','2026-06-03 10:16:20','2026-06-03 10:17:12'),(17,2,1,'2026-06-04','13:00:00','14:00:00',1.00,150000.00,'pending','','03b35aec-0abe-48c3-aa25-de89feb910f2','2026-06-03 10:29:17','2026-06-03 10:29:17'),(18,4,1,'2026-06-05','09:00:00','10:00:00',1.00,150000.00,'pending','','f8a82a38-3238-4457-a9e9-5c305be77956','2026-06-03 11:35:26','2026-06-03 11:35:26');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `court_schedules`
--

DROP TABLE IF EXISTS `court_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `court_schedules` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `court_id` bigint unsigned NOT NULL,
  `day_of_week` tinyint NOT NULL,
  `open_time` time NOT NULL,
  `close_time` time NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_cs_court` (`court_id`),
  CONSTRAINT `fk_cs_court` FOREIGN KEY (`court_id`) REFERENCES `courts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `court_schedules_chk_1` CHECK ((`day_of_week` between 0 and 6))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `court_schedules`
--

LOCK TABLES `court_schedules` WRITE;
/*!40000 ALTER TABLE `court_schedules` DISABLE KEYS */;
INSERT INTO `court_schedules` VALUES (1,1,1,'06:00:00','22:00:00',1),(2,1,2,'06:00:00','22:00:00',1),(3,1,3,'06:00:00','22:00:00',1),(4,1,4,'06:00:00','22:00:00',1),(5,1,5,'06:00:00','22:00:00',1),(6,1,6,'07:00:00','23:00:00',1),(7,1,0,'07:00:00','23:00:00',1),(8,2,1,'06:00:00','22:00:00',1),(9,2,2,'06:00:00','22:00:00',1),(10,2,3,'06:00:00','22:00:00',1),(11,2,4,'06:00:00','22:00:00',1),(12,2,5,'06:00:00','22:00:00',1),(13,2,6,'07:00:00','23:00:00',1),(14,2,0,'07:00:00','23:00:00',1),(15,3,1,'06:00:00','22:00:00',1),(16,3,2,'06:00:00','22:00:00',1),(17,3,3,'06:00:00','22:00:00',1),(18,3,4,'06:00:00','22:00:00',1),(19,3,5,'06:00:00','22:00:00',1),(20,3,6,'07:00:00','23:00:00',1),(21,3,0,'07:00:00','23:00:00',1);
/*!40000 ALTER TABLE `court_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courts`
--

DROP TABLE IF EXISTS `courts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courts` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `price_per_hour` decimal(10,2) NOT NULL,
  `photo_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courts`
--

LOCK TABLES `courts` WRITE;
/*!40000 ALTER TABLE `courts` DISABLE KEYS */;
INSERT INTO `courts` VALUES (1,'Lapangan A','Lapangan indoor standar internasional',150000.00,NULL,1,'2026-05-31 09:38:33','2026-05-31 09:38:33'),(2,'Lapangan B','Lapangan outdoor dengan atap',120000.00,NULL,1,'2026-05-31 09:38:33','2026-05-31 09:38:33'),(3,'Lapangan C','Lapangan VIP, AC full',200000.00,NULL,1,'2026-05-31 09:38:33','2026-05-31 09:38:33');
/*!40000 ALTER TABLE `courts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `title` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('booking_confirmed','booking_rejected','booking_cancelled','payment_verified','reminder') COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_notif_user_unread` (`user_id`,`is_read`,`created_at` DESC),
  CONSTRAINT `fk_n_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,2,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-02 pukul 12:00 menunggu pembayaran.','booking_confirmed',0,'2026-05-31 11:13:57'),(2,2,'Booking Berhasil Dibuat','Booking lapangan Lapangan B pada 2026-06-03 pukul 08:00 menunggu pembayaran.','booking_confirmed',0,'2026-05-31 11:13:57'),(3,2,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-02 pukul 09:00 menunggu pembayaran.','booking_confirmed',0,'2026-05-31 11:39:37'),(4,2,'Bukti Bayar Diterima','Bukti pembayaran booking #4 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-05-31 11:46:33'),(5,2,'Bukti Bayar Diterima','Bukti pembayaran booking #4 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-05-31 11:46:33'),(6,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-06 pukul 08:00 menunggu pembayaran.','booking_confirmed',1,'2026-06-02 14:12:48'),(7,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-05 pukul 16:00 menunggu pembayaran.','booking_confirmed',1,'2026-06-02 14:32:48'),(8,4,'Bukti Bayar Diterima','Bukti pembayaran booking #6 sudah kami terima. Menunggu verifikasi admin.','payment_verified',1,'2026-06-02 14:33:17'),(9,4,'Pembayaran Terverifikasi!','Pembayaran booking lapangan Lapangan A tanggal 2026-06-05 telah diverifikasi. Sampai jumpa di lapangan!','payment_verified',1,'2026-06-02 14:35:56'),(10,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-05 pukul 19:00 menunggu pembayaran.','booking_confirmed',1,'2026-06-03 09:04:53'),(11,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-05 pukul 14:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:05:46'),(12,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-04 pukul 08:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:12:24'),(13,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-04 pukul 10:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:19:47'),(14,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-04 pukul 09:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:21:06'),(15,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-04 pukul 11:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:26:34'),(16,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-04 pukul 07:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:27:03'),(17,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan B pada 2026-06-04 pukul 09:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 09:27:58'),(18,4,'Bukti Bayar Diterima','Bukti pembayaran booking #7 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-06-03 09:46:45'),(19,4,'Bukti Bayar Diterima','Bukti pembayaran booking #5 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-06-03 09:49:27'),(20,4,'Bukti Bayar Diterima','Bukti pembayaran booking #12 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-06-03 09:51:47'),(21,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan C pada 2026-06-05 pukul 08:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 10:06:42'),(22,4,'Bukti Bayar Diterima','Bukti pembayaran booking #15 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-06-03 10:07:06'),(23,4,'Pembayaran Terverifikasi!','Pembayaran booking lapangan Lapangan C tanggal 2026-06-05 telah diverifikasi. Sampai jumpa di lapangan!','payment_verified',0,'2026-06-03 10:08:35'),(24,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-12 pukul 08:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 10:16:20'),(25,4,'Bukti Bayar Diterima','Bukti pembayaran booking #16 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-06-03 10:16:31'),(26,4,'Pembayaran Terverifikasi!','Pembayaran booking lapangan Lapangan A tanggal 2026-06-12 telah diverifikasi. Sampai jumpa di lapangan!','payment_verified',0,'2026-06-03 10:17:12'),(27,2,'Pembayaran Ditolak','Bukti pembayaran booking #4 ditolak. Silakan hubungi admin.','booking_rejected',0,'2026-06-03 10:18:06'),(28,4,'Pembayaran Ditolak','Bukti pembayaran booking #5 ditolak. Silakan hubungi admin.','booking_rejected',0,'2026-06-03 10:18:14'),(29,4,'Pembayaran Ditolak','Bukti pembayaran booking #7 ditolak. Silakan hubungi admin.','booking_rejected',0,'2026-06-03 10:18:14'),(30,2,'Bukti Bayar Diterima','Bukti pembayaran booking #2 sudah kami terima. Menunggu verifikasi admin.','payment_verified',0,'2026-06-03 10:25:38'),(31,2,'Pembayaran Terverifikasi!','Pembayaran booking lapangan Lapangan A tanggal 2026-06-02 telah diverifikasi. Sampai jumpa di lapangan!','payment_verified',0,'2026-06-03 10:26:35'),(32,2,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-04 pukul 13:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 10:29:17'),(33,4,'Booking Berhasil Dibuat','Booking lapangan Lapangan A pada 2026-06-05 pukul 09:00 menunggu pembayaran.','booking_confirmed',0,'2026-06-03 11:35:26');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `booking_id` bigint unsigned NOT NULL,
  `method` enum('transfer','ewallet','cash') COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `proof_image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('unpaid','uploaded','verified','failed') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unpaid',
  `verified_by` bigint unsigned DEFAULT NULL,
  `verified_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `booking_id` (`booking_id`),
  KEY `fk_p_verified_by` (`verified_by`),
  CONSTRAINT `fk_p_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  CONSTRAINT `fk_p_verified_by` FOREIGN KEY (`verified_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,4,'ewallet',150000.00,'booking_4_bd2aa170-6f8b-4902-95d5-135681eb3f33.jpg','failed',NULL,NULL,'2026-05-31 11:39:47','2026-06-03 10:18:06'),(2,6,'ewallet',300000.00,'booking_6_114520f7-0c2f-4428-858a-b1d94ae73c75.jpg','verified',1,'2026-06-02 14:35:56','2026-06-02 14:33:00','2026-06-02 14:35:56'),(3,9,'cash',150000.00,NULL,'unpaid',NULL,NULL,'2026-06-03 09:12:33','2026-06-03 09:12:33'),(4,10,'transfer',150000.00,NULL,'unpaid',NULL,NULL,'2026-06-03 09:20:20','2026-06-03 09:20:20'),(5,11,'transfer',150000.00,NULL,'unpaid',NULL,NULL,'2026-06-03 09:21:14','2026-06-03 09:21:14'),(6,12,'cash',150000.00,'booking_12_a63a107e-3c74-4b7f-becd-5f0f43fa89f8.jpg','uploaded',NULL,NULL,'2026-06-03 09:26:38','2026-06-03 09:51:47'),(7,13,'ewallet',150000.00,NULL,'unpaid',NULL,NULL,'2026-06-03 09:27:13','2026-06-03 09:27:13'),(8,14,'ewallet',120000.00,NULL,'unpaid',NULL,NULL,'2026-06-03 09:28:06','2026-06-03 09:28:06'),(9,7,'ewallet',150000.00,'booking_7_18bf038d-33ad-48d9-9655-544840de4cdf.jpg','failed',NULL,NULL,'2026-06-03 09:38:05','2026-06-03 10:18:14'),(10,5,'ewallet',150000.00,'booking_5_cb6b0783-8401-47d9-989f-94ffb43457cc.jpg','failed',NULL,NULL,'2026-06-03 09:49:27','2026-06-03 10:18:14'),(11,15,'ewallet',200000.00,'booking_15_dc48b2c5-e1bd-4365-a7d1-239669545db2.jpg','verified',1,'2026-06-03 10:08:35','2026-06-03 10:07:06','2026-06-03 10:08:35'),(12,16,'ewallet',150000.00,'booking_16_c2f36c52-998c-4c13-976f-398109a8d754.jpg','verified',1,'2026-06-03 10:17:12','2026-06-03 10:16:30','2026-06-03 10:17:12'),(13,2,'ewallet',150000.00,'booking_2_55a43056-d5f2-4dbe-8b11-77b188ff9d5d.jpg','verified',1,'2026-06-03 10:26:35','2026-06-03 10:25:37','2026-06-03 10:26:35');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('user','admin') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Admin Padel','admin@padel.com','$2a$10$ExFB.ZFIZTXFZtKY9kdjIeLRoJ8gsRofdfRyDd52YZGyoPT.uayyS','081200000001','admin',1,'2026-05-31 09:38:33','2026-05-31 09:44:13'),(2,'Budi Santoso','budi@email.com','$2a$10$j53BO4WUtrzCIs30ZUQ31OcklD4NN7wK7kXjI5Zoj4JlsyRwmBt82','081200000002','user',1,'2026-05-31 09:38:33','2026-05-31 09:44:13'),(3,'Siti Rahayu','siti@email.com','$2a$10$examplehashUSER2','081200000003','user',1,'2026-05-31 09:38:33','2026-05-31 09:38:33'),(4,'Budi Santoso','budi@mail.com','$2a$10$qGSZbKgjx9HLlEXTn5xGDeZ2HE5YjwuBIjyxyMdfdLZQRVIi0bPUW','081234567890','user',1,'2026-05-31 10:53:39','2026-05-31 10:53:39');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-03 12:30:10
