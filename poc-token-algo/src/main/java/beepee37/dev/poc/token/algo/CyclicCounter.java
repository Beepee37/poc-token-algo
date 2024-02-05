package beepee37.dev.poc.token.algo;

import java.util.concurrent.atomic.AtomicInteger;

public class CyclicCounter {

	private final int maxVal;
	private final AtomicInteger counter = new AtomicInteger(0);

	public CyclicCounter(int maxVal) {
		this.maxVal = maxVal;
	}

	public int incrementAndGet() {
		return counter.accumulateAndGet(1, (index, inc) -> (++index >= maxVal ? 0 : index));
	}

}