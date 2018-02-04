alias ig='git rm -r --cached . && git add . && git commit -m "Removing all files in .gitignore"'
alias up='git pull --rebase --prune $@ && git submodule update --init --recursive;'
alias cm='git add -A && git commit -m'
alias save='git add -A && git commit -m 'SAVEPOINT''
