package example01;

import distributed.DistributedSystemNode;

public class Node02 extends DistributedSystemNode {
	public Node02() {
		super("config.properties");
	}
	
	@Override
	public void run() {
		getMessageBrokerClient().subscribe("NODE_DISCONNECTED","NODE02");
	}
	
	public static void main(String[] args) {
		var node = new Node02();
		var t = new Thread(node);
		t.start();
	}
}
