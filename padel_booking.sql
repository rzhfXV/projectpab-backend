-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 01 Jun 2026 pada 09.45
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `padel_booking`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `bookings`
--

CREATE TABLE `bookings` (
  `id` bigint(20) NOT NULL,
  `booking_date` date NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `duration_hours` decimal(4,2) NOT NULL,
  `end_time` time NOT NULL,
  `notes` text DEFAULT NULL,
  `qr_token` varchar(100) DEFAULT NULL,
  `start_time` time NOT NULL,
  `status` varchar(255) NOT NULL,
  `total_price` decimal(12,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `court_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `coaches`
--

CREATE TABLE `coaches` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `coaches`
--

INSERT INTO `coaches` (`id`, `name`, `profile_image_url`, `created_at`) VALUES
(1, 'Deli Rahmansyah', 'deli_profile.png', '2026-05-30 10:24:15'),
(2, 'Carmenita', 'carmen_profile.png', '2026-05-30 10:24:15'),
(3, 'Coach Jessica', 'jessica_profile.png', '2026-05-30 10:24:15');

-- --------------------------------------------------------

--
-- Struktur dari tabel `coach_schedules`
--

CREATE TABLE `coach_schedules` (
  `id` bigint(20) NOT NULL,
  `coach_id` int(11) NOT NULL,
  `session_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `is_booked` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `coach_schedules`
--

INSERT INTO `coach_schedules` (`id`, `coach_id`, `session_date`, `start_time`, `end_time`, `is_booked`) VALUES
(1, 1, '2026-05-30', '06:00:00', '08:00:00', 0),
(2, 1, '2026-05-30', '14:00:00', '16:00:00', 0),
(3, 2, '2026-05-30', '10:00:00', '12:00:00', 0),
(4, 1, '2026-05-31', '15:30:00', '17:30:00', 0),
(5, 2, '2026-05-31', '10:00:00', '17:30:00', 0),
(6, 1, '2026-05-31', '19:00:00', '21:00:00', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `courts`
--

CREATE TABLE `courts` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `photo_url` varchar(500) DEFAULT NULL,
  `price_per_hour` decimal(10,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `rate` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `courts`
--

INSERT INTO `courts` (`id`, `created_at`, `description`, `is_active`, `name`, `photo_url`, `price_per_hour`, `updated_at`, `latitude`, `longitude`, `type`, `rate`) VALUES
(1, NULL, 'Arena padel hits di daerah Kuningan dengan fasilitas lengkap seperti shower, ruang ganti, dan musholla. Tipe: Outdoor.', b'1', 'Padelazo Kuningan', 'lap1', 225000.00, NULL, -6.974664, 108.489212, 'Indoor', 4.8),
(2, NULL, 'Fasilitas premium court di Jakarta Selatan, lengkap dengan cafe dan area lounge luas. Tipe: Indoor.', b'1', 'King Padel by Homeground', 'lap2', 440000.00, NULL, -6.257201, 106.810135, 'Outdoor', 4.9),
(3, NULL, 'Lapangan padel super nyaman dan bersahabat bagi pemula di kawasan Pesanggrahan. Tipe: Outdoor.', b'1', 'Futton Padel Club', 'lap3', 230000.00, NULL, -6.242389, 106.753034, 'Indoor', 4.2),
(4, NULL, 'Destinasi olahraga padel utama di Jakarta Barat dengan fasilitas kaca standar internasional. Tipe: Indoor.', b'1', 'Homeground Padel Kedoya', 'lap4', 250000.00, NULL, -6.187738, 106.760154, 'Outdoor', 4.5),
(5, NULL, 'Bermain padel dengan suasana asri taman di tengah kota Jakarta Barat. Tipe: Outdoor.', b'1', 'Orange Garden Padel Club', 'lap5', 290000.00, NULL, -6.208114, 106.744801, 'Indoor', 4.7);

-- --------------------------------------------------------

--
-- Struktur dari tabel `court_schedules`
--

CREATE TABLE `court_schedules` (
  `id` bigint(20) NOT NULL,
  `close_time` time NOT NULL,
  `day_of_week` tinyint(4) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `open_time` time NOT NULL,
  `court_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `court_schedules`
--

INSERT INTO `court_schedules` (`id`, `close_time`, `day_of_week`, `is_active`, `open_time`, `court_id`) VALUES
(1, '23:59:59', 2, b'1', '06:00:00', 1),
(2, '23:59:59', 3, b'1', '06:00:00', 1),
(3, '23:59:59', 4, b'1', '06:00:00', 1),
(4, '23:59:59', 5, b'1', '06:00:00', 1),
(5, '23:59:59', 6, b'1', '06:00:00', 1),
(6, '23:59:59', 7, b'1', '06:00:00', 1),
(7, '23:59:59', 1, b'1', '06:00:00', 1),
(8, '23:00:00', 2, b'1', '06:00:00', 2),
(9, '23:00:00', 3, b'1', '06:00:00', 2),
(10, '23:00:00', 4, b'1', '06:00:00', 2),
(11, '23:00:00', 5, b'1', '06:00:00', 2),
(12, '23:00:00', 6, b'1', '06:00:00', 2),
(13, '23:00:00', 7, b'1', '06:00:00', 2),
(14, '23:00:00', 1, b'1', '06:00:00', 2),
(15, '23:59:59', 2, b'1', '06:00:00', 3),
(16, '23:59:59', 3, b'1', '06:00:00', 3),
(17, '23:59:59', 4, b'1', '06:00:00', 3),
(18, '23:59:59', 5, b'1', '06:00:00', 3),
(19, '23:59:59', 6, b'1', '06:00:00', 3),
(20, '23:59:59', 7, b'1', '06:00:00', 3),
(21, '23:59:59', 1, b'1', '06:00:00', 3),
(22, '23:00:00', 2, b'1', '06:00:00', 4),
(23, '23:00:00', 3, b'1', '06:00:00', 4),
(24, '23:00:00', 4, b'1', '06:00:00', 4),
(25, '23:00:00', 5, b'1', '06:00:00', 4),
(26, '23:00:00', 6, b'1', '06:00:00', 4),
(27, '23:00:00', 7, b'1', '06:00:00', 4),
(28, '23:00:00', 1, b'1', '06:00:00', 4),
(29, '23:59:59', 2, b'1', '06:00:00', 5),
(30, '23:59:59', 3, b'1', '06:00:00', 5),
(31, '23:59:59', 4, b'1', '06:00:00', 5),
(32, '23:59:59', 5, b'1', '06:00:00', 5),
(33, '23:59:59', 6, b'1', '06:00:00', 5),
(34, '23:59:59', 7, b'1', '06:00:00', 5),
(35, '23:59:59', 1, b'1', '06:00:00', 5);

-- --------------------------------------------------------

--
-- Struktur dari tabel `events`
--

CREATE TABLE `events` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `court_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `events`
--

INSERT INTO `events` (`id`, `description`, `title`, `court_id`) VALUES
(1, '⏰ 08:00 - 12:00 • 📅 Weekend Match. Turnamen antar perusahaan kasual bagi para pemula dan intermediate. Dapatkan kesempatan memenangkan hadiah total senilai Rp 15.000.000, merchandise eksklusif, serta networking makan siang bersama komunitas Padel Jakarta.', 'Padel Corporate Fun Tournament 2026', 1),
(2, '⏰ 19:00 - 22:00 • 📅 Community Night. Sesi fun match khusus pemain wanita (Ladies Only!). Nikmati bermain Padel santai di bawah lampu LED malam hari dengan iringan musik dari Live DJ, gratis sewa raket, minuman isotonik, dan free-flow gelato setelah sesi b', 'Deuce Padel Ladies Friday Night Out', 1),
(3, '⏰ 07:00 - 10:00 • 📅 Sunday Morning. Kelas pelatihan intensif Padel bagi anak-anak dan remaja usia 8-16 tahun. Dipandu langsung oleh pelatih bersertifikasi internasional untuk mengenalkan teknik dasar pukulan, aturan dasar permainan, dan diakhiri dengan mi', 'Junior Padel Coaching Clinic & Exhibition', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `notifications`
--

CREATE TABLE `notifications` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `message` text NOT NULL,
  `title` varchar(150) NOT NULL,
  `type` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `payments`
--

CREATE TABLE `payments` (
  `id` bigint(20) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `method` varchar(255) NOT NULL,
  `proof_image_url` varchar(500) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `verified_at` datetime(6) DEFAULT NULL,
  `booking_id` bigint(20) NOT NULL,
  `verified_by` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `profile_picture` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `created_at`, `email`, `is_active`, `name`, `password`, `phone`, `role`, `updated_at`, `profile_picture`) VALUES
(1, '2026-05-29 23:30:07.000000', 'jeongjaeh8@gmail.com', b'1', 'Mark', '$2a$10$sVaPa2JV.OgpEnxW6DUljO4kLwYlTqrr8WrujeNJHFq0FegniWFFK', '082116480589', 'USER', '2026-05-29 23:30:07.000000', NULL),
(2, '2026-05-29 23:32:41.000000', 'noturmalika43@gmail.com', b'1', 'Malika', '$2a$10$bYWBnE9/CYyhh7/uC/GRReTStsIeZbGOyMKQxy.T/cUp/7pTNQZq.', '082116480589', 'USER', '2026-05-29 23:32:41.000000', NULL);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK7am73665c85ewokrlt864jv84` (`qr_token`),
  ADD KEY `FKrc2ar1jl63nymaipwibvab7q0` (`court_id`),
  ADD KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`);

--
-- Indeks untuk tabel `coaches`
--
ALTER TABLE `coaches`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `coach_schedules`
--
ALTER TABLE `coach_schedules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `coach_id` (`coach_id`);

--
-- Indeks untuk tabel `courts`
--
ALTER TABLE `courts`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `court_schedules`
--
ALTER TABLE `court_schedules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKng5egae5us8jm646ld017fxku` (`court_id`);

--
-- Indeks untuk tabel `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfggdk9uhn6uljmo1cpd7ucn30` (`court_id`);

--
-- Indeks untuk tabel `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9y21adhxn0ayjhfocscqox7bh` (`user_id`);

--
-- Indeks untuk tabel `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKnuscjm6x127hkb15kcb8n56wo` (`booking_id`),
  ADD KEY `FKo8mhcr0oovd4urrml4mg0qgwq` (`verified_by`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `coaches`
--
ALTER TABLE `coaches`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `coach_schedules`
--
ALTER TABLE `coach_schedules`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `courts`
--
ALTER TABLE `courts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `court_schedules`
--
ALTER TABLE `court_schedules`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT untuk tabel `events`
--
ALTER TABLE `events`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKrc2ar1jl63nymaipwibvab7q0` FOREIGN KEY (`court_id`) REFERENCES `courts` (`id`);

--
-- Ketidakleluasaan untuk tabel `coach_schedules`
--
ALTER TABLE `coach_schedules`
  ADD CONSTRAINT `coach_schedules_ibfk_1` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `court_schedules`
--
ALTER TABLE `court_schedules`
  ADD CONSTRAINT `FKng5egae5us8jm646ld017fxku` FOREIGN KEY (`court_id`) REFERENCES `courts` (`id`);

--
-- Ketidakleluasaan untuk tabel `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `FKfggdk9uhn6uljmo1cpd7ucn30` FOREIGN KEY (`court_id`) REFERENCES `courts` (`id`);

--
-- Ketidakleluasaan untuk tabel `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FKc52o2b1jkxttngufqp3t7jr3h` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `FKo8mhcr0oovd4urrml4mg0qgwq` FOREIGN KEY (`verified_by`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
