Pas de push direct sur `dev` ou `main`, on passe par des PR.

1. Créer ta branche depuis `dev` : `feature/...` ou `fix/...`
2. Commiter comme on veut, pousser, ouvrir une PR vers `dev`
3. Le titre de la PR doit ressembler à `feat: ajoute le mode cuisine` ou
   `fix: corrige les quantités`, sinon c'est bloqué
4. Si c'est vert, on merge. Tous les commits sont regroupés en un seul et la
   branche est supprimée du repos

Pour mettre sur `main`, il faut l'accord de l'autre :

```bash
git fetch origin
git switch -c release/1.0 origin/dev
git rebase origin/main
git push -u origin release/1.0
```

Puis une PR vers `main`.
