package wolox.bootstrap.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class MomentOfDayProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		int hourOfDay = DateTime.now().getHourOfDay();

		String momentOfDay;

		if (hourOfDay >= 5 && hourOfDay <= 12) {
			momentOfDay = "morning";
		} else if (hourOfDay > 12 && hourOfDay <= 18) {
			momentOfDay = "afternoon";
		} else {
			momentOfDay = "night";
		}

		exchange.getIn().setHeader("momentOfDay", momentOfDay);
	}
}
