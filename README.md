# coms430
Advanced Programming Tools @ ISU

## Parallel Streams Presentation
The class is special in that it requires students to teach the class ~50% of the time. For my portion of teaching I spoke about
Java parallel streams. First, I covered how easily parallelization is to add into a serial stream. Then I went more in-depth
to analyzing the Spliterator and some of the advantages/disadvantages to using different implementation techniques. After this
I covered ***some details about Collectors***. Finally, I introduced the problems that can arise from parallel streams such
as common parallelization issues that developers often overlook in streams and long-running operations during streams.

## HW1

Focused on creating thread-safe classes. This included concepts of mutability, publishing, mutexes, visibility, 
and happens-before relationships. Written in Java, my answers are in the folders a-e, original code is in og_code, and 
the assignment itself is in hw1.pdf.

## HW2

Primarily stressed interactions between threads and common design patterns around that. Specifically, interactions between
auxilairy threads and the EDT (Event Dispatch Thread), use of concurrent data structures in solving common problems, and
an inside look at how to handle readers and writers in a list data structure.
