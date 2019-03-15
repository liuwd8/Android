DROP TABLE IF EXISTS `equip`;
CREATE TABLE `equip` (
  `equip_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `equip_type` int(11) DEFAULT NULL,
  `selling_price` int(11) DEFAULT NULL,
  `purchase_price` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `passive` varchar(255) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`equip_id`)
);

DROP TABLE IF EXISTS `hero_equip`;
CREATE TABLE `hero_equip` (
  `hero_id` int(11) NOT NULL,
  `equip_ids1` varchar(255) DEFAULT NULL,
  `tips1` text,
  `equip_ids2` varchar(255) DEFAULT NULL,
  `tips2` text,
  PRIMARY KEY (`hero_id`)
);

DROP TABLE IF EXISTS `hero`;
CREATE TABLE `hero` (
  `hero_id` integer NOT NULL,
  `name` text DEFAULT NULL,
  `pay_type` integer DEFAULT NULL,
  `new_type` integer DEFAULT NULL,
  `hero_type` integer DEFAULT NULL,
  `hero_type2` integer DEFAULT NULL,
  `skin_name` text DEFAULT NULL,
  `img_url` text  DEFAULT NULL,
  `live` integer DEFAULT NULL,
  `attack` integer DEFAULT NULL,
  `skill` integer DEFAULT NULL,
  `difficulty` integer DEFAULT NULL,
  PRIMARY KEY (`hero_id`)
);

DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `skill_id` int(11) NOT NULL,
  `hero_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `cool` int(11) DEFAULT NULL,
  `waste` int(11) DEFAULT NULL,
  `description` text,
  `tips` text,
  `img_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skill_id`)
);

DROP TABLE IF EXISTS `skin`;
CREATE TABLE `skin` (
  `skin_id` int(11) NOT NULL,
  `hero_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skin_id`)
);

DROP TABLE IF EXISTS `present`;
CREATE TABLE `present` (
    `hero_id` int(11) NOT NULL,
    `name` varchar(255) NOT NULL,
    `hero_type` varchar(255) NOT NULL,
    PRIMARY KEY(`hero_id`)
);

DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
    `hero_id` int(11) NOT NULL,
    `name` varchar(255) NOT NULL,
    `partner1` varchar(255) NOT NULL,
    `partner1description` varchar(1024) NOT NULL,
    `partner2` varchar(255) NOT NULL,
    `partner2description` varchar(1024) NOT NULL,
    `repress1` varchar(255) NOT NULL,
    `repress1description` varchar(1024) NOT NULL,
    `repress2` varchar(255) NOT NULL,
    `repress2description` varchar(1024) NOT NULL,
    `repressed1` varchar(255) NOT NULL,
    `repressed1description` varchar(1024) NOT NULL,
    `repressed2` varchar(255) NOT NULL,
    `repressed2description` varchar(1024) NOT NULL,
    PRIMARY KEY(`hero_id`)
);