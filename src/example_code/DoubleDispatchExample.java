package dispatch.double_dispatch;

//
// We want to use Java's dynamic binding mechanism so that the 
// in the component's send() method, the correct version of 
// handle(SomeMessage m) is invoked, depending on the runtime type of m.
//
// Dynamic binding of a method only happens based on the method target,
// not the method arguments.  So a call to c.handle(m) 
// (c is target, m is argument) has to be turned around into a call
// of the form m.dispatch(c).  This is called "double dispatch" or
// the Visitor pattern.
//
// Each message type has an implementation of the dispatch method of the form,
//
//  public void dispatch(Component c)
//  {
//    c.handle(this);
//  }
//
// and each component has a corresponding method of the form
//
//  public void handle(SomeMessage m)
//  {
//     ...do the real work of handling the message
//  }
//
// It is less confusing if, rather than overloading a method called handle(),
// we just call the method something unique such as,
//
//  public void handleSomeMessage(SomeMessage m)
//  {
//    ...do the real work of handling the message
//  }
//
// and likewise in the SomeMessage type we define dispatch() like this,
//
//  public void dispatch(Component c)
//  {
//    c.handleSomeMessage(this);
//  }
//
// So a handleXXX() method has to be present in the component supertype for
// every possible message type.  (This is true whether or not we choose to
// overload the method name 'handle'.)
//
// In summary, to set up a component type SomeComponent to handle a message
// type SomeMessage:
// 
//  1) Define SomeMessage as a subtype of IMessage, and include
// an implementation of the dispatch() method as above
//
//  2) Add a method to the Component supertype of the form,
//
//   public void handleSomeMessage(SomeMessage m)
//   {
//      handleDefault(m);
//   }
//
//  3) Add an override to the SomeComponent class to do the real
// message handling,
//
//   public void handleSomeMessage(SomeMessage m)
//   {
//     ...do the real work of handling the message
//   }
//
// (Aside: even if you overload the same name 'handle' for all the 
// message handling methods, you still need to include the override
// of the dispatch() method in every concrete message type.  
// That is to ensure that the *compile-time* type of 'this' 
// is correct, so that the compiler selects the correct overload
// of handle()).
//
public class DoubleDispatchExample
{
  
  public static void main(String[] args)
  {
    IMessage m = new Message1();
    IMessage m2 = new Message2();

    Component c = new Component();
    c.send(m);
    c.send(m2);

    c = new SubComponent();
    c.send(m);
    c.send(m2);

  }
}

class Component
{
  public void send(IMessage m)
  {
    // use "double dispatch"
    m.dispatch(this);
  }
  
  // For this strategy to work, a handle() method
  // has to be present in the base class for every possible
  // message type.
  
  public void handleDefault(IMessage msg)
  {
    System.out.print("Default handler in base Component, ");
    System.out.println(msg);
  }
  
  public void handleMessage1(Message1 msg)
  {
    System.out.print("Handler 1 in base Component, ");
    System.out.println(msg);
  }
  
  public void handleMessage2(Message2 msg)
  {
    // defer to default handler if not overridden
    handleDefault((IMessage) msg);  
  } 
}

class SubComponent extends Component
{
  @Override
  public void handleMessage2(Message2 msg)  
  {
    System.out.print("Handler 2 in Component2, ");
    System.out.println(msg);
  }
}


interface IMessage
{
  void dispatch(Component c);
}

class Message1 implements IMessage
{
  @Override
  public void dispatch(Component c)
  {
    c.handleMessage1(this);  
  }

  public String toString()
  {
    return "CubeMessage 1";
  }  
}

class Message2 implements IMessage
{
  @Override
  public void dispatch(Component c)
  {
    c.handleMessage2(this);
  }

  public String toString()
  {
    return "CubeMessage 2";
  }
}
