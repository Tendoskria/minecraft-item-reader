# ItemReader Mod pour Minecraft

**ItemReader** est un mod pour Minecraft qui vous permet de récupérer rapidement des informations détaillées sur les objets que vous tenez dans la main. Que ce soit le nom personnalisé de l'objet, ses enchantements, ou même ses informations spéciales dans le lore, ce mod génère un **fichier JSON** que vous pouvez facilement copier dans votre presse-papiers.

## Fonctionnalités principales

- **Récupérer des informations sur un objet** : En appuyant sur une touche, le mod extrait les informations de l'objet que vous tenez et génère un fichier JSON avec ces données.
- **Inclut le nom personnalisé** : Si votre objet a un nom personnalisé, il sera inclus dans le JSON.
- **Enchanteurs à l'honneur** : Les enchantements de l'objet sont aussi extraits, avec leur niveau et leur couleur.
- **Lore et enchantements personnalisés** : Si votre objet a une histoire ou des enchantements personnalisés dans son lore, ceux-ci seront également pris en compte.

## Version minimale supportée

Le mod ItemReader est compatible à partir de la version Minecraft 1.21.0. Veuillez vous assurer que vous utilisez cette version ou une version plus récente pour garantir le bon fonctionnement du mod.

## Installation

1. **Téléchargez et installez Fabric** :
    - Si vous ne l'avez pas déjà fait, installez [Fabric](https://fabricmc.net/use/).
    - Vous devrez également télécharger et installer le **Fabric API** pour que le mod fonctionne correctement.

2. **Téléchargez le mod** :
   - Vous pouvez télécharger le mod (fichier `.jar`) de **ItemReader** directement depuis [ce lien GitHub](https://github.com/Tendoskria/minecraft-item-reader/releases/tag/1.0.1).

3. **Placez le fichier du mod** :
    - Déplacez le fichier `.jar` dans le dossier `mods` de votre installation Minecraft.
    - Le chemin habituel est : `C:\Users\NomUtilisateur\AppData\Roaming\.minecraft\mods`.

4. **Lancez Minecraft avec Fabric** :
    - Ouvrez le lanceur Minecraft et assurez-vous de sélectionner le profil **Fabric** pour lancer le jeu.

## Utilisation

1. **Ouvrez Minecraft et tenez un objet** dans votre main.
2. **Appuyez sur la touche définie** pour récupérer les informations. Par défaut, la touche est `G`, mais vous pouvez la configurer dans les paramètres du jeu.

3. **Le JSON est copié dans votre presse-papiers** ! Une fois la touche pressée, toutes les informations détaillées de l'objet seront copiées dans votre presse-papiers sous forme de JSON. Vous pouvez ensuite les coller où vous le souhaitez.

   Exemple de ce que vous pourriez obtenir :

   ```json
   {
       "minecraft:item_name": "minecraft:diamond_sword",
       "minecraft:custom_name": "§7Épée légendaire",
       "minecraft:lore": "<p><span style=\"color: rgb(255, 255, 255);\">L'épée qui a traversé le temps.</span></p>",
       "minecraft:enchantments": [
           {
               "name": "Sharpness",
               "level": 5,
               "color": "rgb(170, 170, 170)"
           },
           {
               "name": "Unbreaking",
               "level": 3,
               "color": "rgb(170, 170, 170)"
           }
       ]
   }
    ```
   
4. **C'est tout** ! Vous avez maintenant un fichier JSON avec toutes les informations de l'objet que vous tenez.

## Personnalisation des raccourcis

Si vous voulez changer la touche utilisée pour récupérer les informations de l'objet :

1. Allez dans le menu Options de Minecraft.

2. Cliquez sur Contrôles.

3. Cherchez la ligne ItemReader: Get Item Text et changez la touche à votre convenance.

## Dépannage

Si vous avez des problèmes avec le mod :

- Aucune information n'est copiée : Assurez-vous de bien tenir un objet dans votre main et d'avoir appuyé sur la bonne touche.

- Le mod ne fonctionne pas : Vérifiez que vous avez installé Fabric et que vous avez placé le fichier .jar du mod dans le bon dossier mods.

## Contribution

Vous pouvez également contribuer au développement de ce mod ! Si vous avez des idées, des corrections de bugs ou des améliorations à proposer, vous pouvez forker le projet et créer une pull request.
