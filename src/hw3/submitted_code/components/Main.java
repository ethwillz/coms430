package hw3.submitted_code.components;

/**
 * Start up a system with input, client, proxy components.
 * Note that SimpleServer should be running on port 2222.
 */
public class Main
{
  public static void main(String[] args)
  {
    ProxyWorker worker = new ProxyWorker();
    TimerComponent timer = new TimerComponent();
    Component proxy = new ProxyComponent(worker);
    Component client = new ClientComponent(proxy, timer);
    Component input = new InputComponent(client);

    proxy.start();
    worker.start();
    client.start();
    input.start();
  }

}
