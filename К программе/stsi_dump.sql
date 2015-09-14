-- phpMyAdmin SQL Dump
-- version 4.2.5
-- http://www.phpmyadmin.net
--
-- Хост: localhost:8889
-- Время создания: Дек 09 2014 г., 18:16
-- Версия сервера: 5.5.38
-- Версия PHP: 5.5.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `stsi`
--
create database stsi;
use stsi;
-- --------------------------------------------------------

--
-- Структура таблицы `car`
--

CREATE TABLE `car` (
`car_id` int(10) unsigned NOT NULL,
  `brand` varchar(45) NOT NULL,
  `model` varchar(45) NOT NULL,
  `manufact_year` year(4) NOT NULL,
  `owner_id` int(10) unsigned NOT NULL,
  `color_id` int(10) unsigned NOT NULL,
  `complectation_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Дамп данных таблицы `car`
--

INSERT INTO `car` (`car_id`, `brand`, `model`, `manufact_year`, `owner_id`, `color_id`, `complectation_id`) VALUES
(1, 'audi', 'a5', 2006, 1, 1, 3),
(2, 'ford', 'focus', 2001, 1, 2, 5),
(3, 'bmw', 'm3', 2012, 2, 3, 5),
(4, 'audi', 'a5', 2000, 3, 4, 6),
(5, 'mercedes-benz', 'C-class', 2014, 4, 10, 5),
(6, 'toyota', 'rav4', 1996, 5, 1, 5),
(7, 'honda', 'cr-v', 2002, 6, 2, 6),
(8, 'toyota', 'lc prado', 2013, 7, 3, 6),
(9, 'nissan', 'teana', 2004, 8, 3, 5),
(10, 'toyota', 'crown', 1999, 9, 8, 2),
(11, 'honda', 'accord', 2003, 10, 9, 1),
(12, 'kamaz', '65201', 2006, 10, 9, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `color`
--

CREATE TABLE `color` (
`color_id` int(10) unsigned NOT NULL,
  `color` varchar(45) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Дамп данных таблицы `color`
--

INSERT INTO `color` (`color_id`, `color`) VALUES
(1, 'белый'),
(2, 'красный'),
(3, 'черный'),
(4, 'серебристый'),
(5, 'зеленый'),
(7, 'желтый'),
(8, 'золотой'),
(9, 'синий'),
(10, 'серый'),
(11, 'коричневый'),
(12, 'оранжевый');

-- --------------------------------------------------------

--
-- Структура таблицы `complectation`
--

CREATE TABLE `complectation` (
`complectation_id` int(11) NOT NULL,
  `gearbox` varchar(45) DEFAULT NULL,
  `drive` varchar(45) DEFAULT NULL,
  `body` varchar(45) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `complectation`
--

INSERT INTO `complectation` (`complectation_id`, `gearbox`, `drive`, `body`) VALUES
(1, 'ручная', '2WD', 'пассажирский'),
(2, 'ручная', '4WD', 'пассажирский'),
(3, 'ручная', '2WD', 'грузовой'),
(4, 'ручная', '4WD', 'грузовой'),
(5, 'автоматическая', '2WD', 'пассажирский'),
(6, 'автоматическая', '4WD', 'пассажирский'),
(7, 'автоматическая', '2WD', 'грузовой'),
(8, 'автоматическая', '4WD', 'грузовой');

-- --------------------------------------------------------

--
-- Структура таблицы `fine`
--

CREATE TABLE `fine` (
`fine_id` int(11) NOT NULL,
  `date_and_time` datetime NOT NULL,
  `amount` int(11) NOT NULL,
  `owner_id` int(10) unsigned NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=74 ;

--
-- Дамп данных таблицы `fine`
--

INSERT INTO `fine` (`fine_id`, `date_and_time`, `amount`, `owner_id`) VALUES
(1, '2014-10-06 08:33:00', 1000, 2),
(2, '2014-08-15 12:39:00', 6000, 5),
(3, '2014-03-30 06:23:00', 800, 1),
(4, '2014-04-29 16:26:00', 300, 1),
(5, '2014-11-25 03:58:00', 2000, 2),
(6, '2014-03-23 09:59:00', 5000, 3),
(7, '2014-02-12 05:34:00', 100, 10),
(8, '2014-09-13 19:52:00', 500, 10),
(9, '2014-08-19 21:25:00', 1000, 5),
(10, '2014-05-18 22:37:00', 2000, 5),
(11, '2014-08-28 16:27:00', 1500, 5),
(12, '2014-02-10 15:15:00', 100, 1),
(13, '2014-07-12 14:33:00', 800, 6),
(14, '2014-10-29 13:51:00', 600, 7),
(15, '2014-10-13 12:18:00', 500, 3),
(44, '2014-10-13 12:21:00', 500, 3),
(45, '2014-10-13 12:32:00', 500, 3),
(72, '2014-10-06 08:39:00', 1000, 2),
(73, '2014-12-09 19:38:00', 120, 10);

-- --------------------------------------------------------

--
-- Структура таблицы `owner`
--

CREATE TABLE `owner` (
`owner_id` int(10) unsigned NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `birth_date` date NOT NULL,
  `driving_permit` varchar(10) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- Дамп данных таблицы `owner`
--

INSERT INTO `owner` (`owner_id`, `first_name`, `last_name`, `birth_date`, `driving_permit`) VALUES
(1, 'Дмитрий', 'Лазукин', '1992-07-18', '5618016184'),
(2, 'Александр', 'Ларин', '1990-04-02', '6402451933'),
(3, 'Семен', 'Пагин', '1994-05-06', '6506482800'),
(4, 'Владислав', 'Панов', '1989-05-06', '1806002566'),
(5, 'Артур', 'Алиев', '1984-02-21', '3828082001'),
(6, 'Константин', 'Абрамов', '1979-05-03', '0203288051'),
(7, 'Максим', 'Пугачев', '1995-12-26', '6426944042'),
(8, 'Филипп', 'Адеев', '1976-07-10', '7710124380'),
(9, 'Владимир', 'Ким', '1982-01-14', '6914619042'),
(10, 'Кирилл', 'Акуленко', '1965-02-12', '7712035993');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `car`
--
ALTER TABLE `car`
 ADD PRIMARY KEY (`car_id`), ADD UNIQUE KEY `car_id_UNIQUE` (`car_id`), ADD KEY `fk_car_color1_idx` (`color_id`), ADD KEY `fk_car_owner1_idx` (`owner_id`), ADD KEY `fk_car_complectation1_idx` (`complectation_id`);

--
-- Indexes for table `color`
--
ALTER TABLE `color`
 ADD PRIMARY KEY (`color_id`), ADD UNIQUE KEY `color_id_UNIQUE` (`color_id`);

--
-- Indexes for table `complectation`
--
ALTER TABLE `complectation`
 ADD PRIMARY KEY (`complectation_id`), ADD UNIQUE KEY `complectation_id_UNIQUE` (`complectation_id`);

--
-- Indexes for table `fine`
--
ALTER TABLE `fine`
 ADD PRIMARY KEY (`fine_id`), ADD UNIQUE KEY `fine_id_UNIQUE` (`fine_id`), ADD KEY `fk_fine_owner_idx` (`owner_id`);

--
-- Indexes for table `owner`
--
ALTER TABLE `owner`
 ADD PRIMARY KEY (`owner_id`), ADD UNIQUE KEY `owner_id_UNIQUE` (`owner_id`), ADD UNIQUE KEY `driving_permit_UNIQUE` (`driving_permit`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `car`
--
ALTER TABLE `car`
MODIFY `car_id` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `color`
--
ALTER TABLE `color`
MODIFY `color_id` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `complectation`
--
ALTER TABLE `complectation`
MODIFY `complectation_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `fine`
--
ALTER TABLE `fine`
MODIFY `fine_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=74;
--
-- AUTO_INCREMENT for table `owner`
--
ALTER TABLE `owner`
MODIFY `owner_id` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `car`
--
ALTER TABLE `car`
ADD CONSTRAINT `fk_car_color1` FOREIGN KEY (`color_id`) REFERENCES `color` (`color_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_car_complectation1` FOREIGN KEY (`complectation_id`) REFERENCES `complectation` (`complectation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_car_owner1` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`owner_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `fine`
--
ALTER TABLE `fine`
ADD CONSTRAINT `fk_fine_owner` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`owner_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
