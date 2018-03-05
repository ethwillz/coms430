package hw3.submitted_code.syntaxtree;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Singleton class for managing the abstract syntax trees obtained
 * from parsing source files.
 * 
 * In this version, a sync lock is held
 * on the SyntaxTreeManager for the duration of every
 * call to the parser.
 */
public class SyntaxTreeManager
{
  
  /**
   * The actual cache of syntax trees
   */
  private final ConcurrentMap<String, FutureTask<SyntaxTree>> m_cache = new ConcurrentHashMap<>();
  
  /**
   * Return the syntax tree for the given source file, parsing
   * the file if necessary.  
   * 
   * @param filename
   * @return
   */
  public SyntaxTree getSyntaxTree(String filename) throws ExecutionException, InterruptedException {
      FutureTask<SyntaxTree> cachedTree = m_cache.computeIfAbsent(filename,
              key -> new FutureTask<>(() -> new Parser(filename).parse()));
      cachedTree.run();
      return cachedTree.get();
  }
}
