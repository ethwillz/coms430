# All new aliases should be 'locked in' with command 'source .bash_profile'

alias ig='git rm -r --cached . && git add . && git commit -m "Removing all files in .gitignore"'
alias up='git pull --rebase --prune $@ && git submodule update --init --recursive;'
alias cm='git add -A && git commit -m'
alias push='git push origin'
alias save='git add -A && git commit -m 'SAVEPOINT''
alias amend='commit -a --amend'
alias stat='git status'
