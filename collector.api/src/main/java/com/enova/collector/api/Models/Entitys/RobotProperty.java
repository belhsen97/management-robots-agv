package com.enova.collector.api.Models.Entitys;


import com.enova.collector.api.Enums.TypeProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "robotproperty")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RobotProperty implements Serializable {
    @Id
    String id;
    String name;
    Date timestamp;
    TypeProperty type;
    String value;
}
/*
 stocker une grande quantité de données pour chaque robot et que vous avez besoin de gérer et d'interroger ces propriétés de manière efficace.
les avantages de cette approche :

    Modularité : La séparation des propriétés du robot dans leur propre classe permet à votre base de code d'être organisée et modulaire. Chaque classe représente une entité spécifique (les propriétés du robot), ce qui facilite la compréhension et la maintenance.

    Évolutivité : En structurant les données dans des classes distinctes, vous pouvez faire évoluer efficacement votre système pour gérer un grand nombre de robots et leurs propriétés. Cette approche vous permet de gérer et d'interroger les propriétés de manière indépendante, ce qui peut améliorer les performances et l'évolutivité.

    Flexibilité : L'utilisation d'une classe distincte pour les propriétés des robots vous offre une certaine souplesse dans la gestion des différents types de propriétés et de leurs valeurs. Vous pouvez définir la structure de chaque propriété et sa valeur de manière indépendante, ce qui vous permet d'utiliser un large éventail de types et de formats de données.

    Interrogation et analyse : Le stockage des propriétés des robots en tant qu'entités distinctes dans la base de données permet d'effectuer des recherches et des analyses efficaces. Vous pouvez facilement récupérer et analyser des propriétés spécifiques pour des robots individuels ou effectuer des requêtes globales sur plusieurs robots.

    Facilité de maintenance : Avec une séparation claire des préoccupations, votre base de code devient plus facile à maintenir. Les modifications apportées à la structure ou au comportement des propriétés des robots peuvent être effectuées indépendamment des autres parties du système, ce qui réduit le risque d'effets secondaires involontaires.

Traduit avec DeepL.com (version gratuite)*/