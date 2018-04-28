-module(histogram).
-export([
   merge/2,
   make/2,
   make/3,
   worker/3,
   from_samples/1,
   try_it/0,
   try_it/3,
   count_samples/1
 ]).

%% @doc Calls make/3 with the given arguments while timing it. Once this call to
%% make/3 finishes, the following are printed to the terminal: the computed
%% histogram, the time elapsed to compute it, and the total number of samples.
try_it(NumSamples, UpperBound, NumActors) ->
  F = fun() -> make(NumSamples, UpperBound, NumActors) end,
  {Histogram, Elapsed} = stopwatch:time_it(F),
  io:fwrite("~w~n", [Histogram]),
  io:fwrite("Elapsed Time: ~wms~n", [Elapsed]),
  ok.

%% @doc Calls try_it/3 with some default values.
try_it() ->
  try_it(100, 100, 4).

%% @doc Makes a histogram with the given number of samples and upper bound using
%% the given number of workers running concurrently. Essentially, this spawns
%% this number of worker actors and then merges their results as they arrive.
make(NumSamples, UpperBound, NumWorkers)->
  make(NumSamples, UpperBound, NumWorkers, NumWorkers, self()),
  receive
    {newResult, Histogram} ->
      io:format("creating master with ~w~n", [Histogram]),
      listener(Histogram, NumSamples, NumWorkers - 2)
  end.

listener(ResultsReceiver, _, Counter) when Counter == 0 ->
  receive
    {newResult, Histogram} ->
      NewList = merge(ResultsReceiver, Histogram),
      NewList
  end;
listener(ResultsReceiver, NumSamples, Counter) ->
  receive
    {newResult, Histogram} ->
      NewList = merge(ResultsReceiver, Histogram),
      listener(NewList, NumSamples, Counter - 1)
  end.

make(_, _, _, Counter, _) when Counter == 0 ->
  all_spawned;
make(NumSamples, UpperBound, NumWorkers, Counter, ResultsReceiver) when Counter > 0 ->
  spawn(histogram, worker, [NumSamples div NumWorkers, UpperBound div NumWorkers, ResultsReceiver]),
  make(NumSamples, UpperBound, NumWorkers, Counter - 1, ResultsReceiver).

%% @doc Makes a histogram.
make(NumSamples, UpperBound) ->
  Samples = randomlists:make(NumSamples, UpperBound),
  SortedSamples = lists:sort(Samples),
  from_samples(SortedSamples).

%% @doc A helper function which is meant to be spawned by make/3 as a process
%% to run make/2. It Calls make/2 with the given parameters and then sends the
%% results to ResultsReceiver.
worker(NumSamples, UpperBound, ResultsReceiver) ->
  NewSamples = make(NumSamples, UpperBound),
  ResultsReceiver ! {newResult, NewSamples},
  ok.

%% @doc Creates a histogram from a list of integer samples.
from_samples([Head | Tail]) ->
  from_samples(Tail, Head, 1, []).

from_samples([], Cur, Count, Histogram) ->
  [{Cur, Count} | Histogram];
from_samples([Head | Tail], Cur, Count, Histogram) when Head == Cur ->
  from_samples(Tail, Head, Count + 1, Histogram);
from_samples([Head | Tail], Cur, Count, Histogram) when Head =/= Cur ->
  from_samples(Tail, Head, 1, [ {Cur, Count} | Histogram]).

%% @doc Merges two histograms together.
merge([], [Head | Tail], Histogram, Counter) ->
  {_, Count} = Head,
  merge([], Tail, [{Counter, Count} | Histogram], Counter + 1);
merge([], [], Histogram, _) ->
  Histogram;
merge([Head | Tail], Ys, Histogram, Counter) ->
  {_, Count} = Head,
  merge(Tail, Ys, [{Counter, Count} | Histogram], Counter + 1).

merge(Xs, Ys) ->
  [Head | Tail] = Xs,
  {_, Count} = Head,
  Counter = 0,
  merge(Tail, Ys, [{Counter, Count}], Counter + 1).

%% @doc Counts the total number of samples in the given histogram. This is just
%% the sum of all of the values in all of the 2-tuples.
count_samples(Histogram) ->
  [Head | Tail] = Histogram,
  {_, Count} = Head,
  count_samples(Tail, Count).

count_samples([], Accumulator) ->
  Accumulator;
count_samples([Head | Tail], Accumulator) ->
  {_, Count} = Head,
  count_samples(Tail, Accumulator + Count).
