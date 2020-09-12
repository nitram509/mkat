use mkat;

CREATE TABLE `languages` (
                             `id` int NOT NULL,
                             `name` varchar(45) DEFAULT NULL,
                             `short` varchar(2) DEFAULT NULL,
                             `image_path` varchar(200) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `languages` (`id`,`name`,`short`,`image_path`) VALUES (1,'deutsch','de','l_de.png');
INSERT INTO `languages` (`id`,`name`,`short`,`image_path`) VALUES (2,'französisch','fr','l_fr.png');
