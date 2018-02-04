alias gitignore='git rm -r --cached . && git add . && git commit -m "Removing all files in .gitignore"'
alias gitpull='git pull --rebase --prune $@ && git submodule update --init --recursive;'
