-module(nodups).
-export([try_it/0, nodups/2]).

nodups(El, List) ->
  case lists:member(El, List) of
    true -> List;
    false -> [El | List]
  end.

nodups(Xs) -> lists:reverse(lists:foldl(fun nodups/2, [], Xs)).

try_it() ->
  nodups(lists:sort(randomlists:prepend(10, 2))).
