# Computer Science 430 at ISU
## Advanced Programming Tools

### Parallel Streams Presentation
The class is special in that it requires students to teach the class ~50% of the time. For my portion of teaching I spoke about
Java parallel streams. I first discussed the many performance benefits of parallel streams and how this performance was achieved through the use of the Spliterator. Next, I covered the Collector class which is widely used in mutable reductions for streams. Finally, I ended with practical considerations; common mistakes that developers make which can hurt or destroy stream performance.

### HW1

Focused on creating thread-safe classes. This included concepts of mutability, publishing, mutexes, visibility, 
and happens-before relationships. Written in Java, my answers are in the folders a-e, original code is in og_code, and 
the assignment itself is in hw1.pdf.

### HW2

Primarily stressed interactions between threads and common design patterns around that. Specifically, interactions between
auxilairy threads and the EDT (Event Dispatch Thread), use of concurrent data structures in solving common problems, and
an inside look at how to handle readers and writers in a list data structure.

### HW3

The first activity (Histogram.java) explored conducting sampling on subranges and aggregating them when threads completed processing.

The second activity was in the syntaxtree directory. This simulated a filetree where different files must be parsed. The rules adhered to for performance are in the PDF.

In the components section we explored the basics of the double-dispatch pattern we covered in class. Finally, building on that we made a basic version of a Yahtzee game based on the double-dispatch pattern. The functionality of this game is detailed in the PDF.
