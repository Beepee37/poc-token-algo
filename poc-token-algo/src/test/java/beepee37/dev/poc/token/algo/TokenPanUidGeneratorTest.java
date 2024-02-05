package beepee37.dev.poc.token.algo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TokenPanUidGeneratorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenPanUidGeneratorTest.class);

	@Test
	void test() throws DecoderException {
		TokenPanUidGenerator tupg = new TokenPanUidGenerator('b', (short) 132, (short) 217);
		String pan = "1234567890123456";
		String token = tupg.GenerateTokenFromPan(pan);
		LOGGER.info("Pan [{}] token [{}] ", pan, token);

		LOGGER.debug("UUID encoded b64 12 octets input {}",
				Base64.getEncoder().encodeToString(Hex.decodeHex("123456789012345699123480")));

		token = tupg.GenerateTokenFromPan(pan);
		LOGGER.info("Pan [{}] token [{}] ", pan, token);
	}

	@Test
	void test2() throws DecoderException {
		TokenPanUidGenerator tupg = new TokenPanUidGenerator('c', (short) 12, (short) 1);
		String pan = "1234567890123456789";
		String token = tupg.GenerateTokenFromPan(pan);
		LOGGER.info("Pan [{}] token [{}] ", pan, token);

	}

	@Test
	void test4() {

		LocalDateTime START_DATE_TIME_INITIAL_POINT = LocalDateTime.of(2022, 12, 31, 0, 0);
		long localNanoInterval = START_DATE_TIME_INITIAL_POINT.until(LocalDateTime.now(), ChronoUnit.NANOS);
		long localNanoInterval2 = START_DATE_TIME_INITIAL_POINT.until(LocalDateTime.now(), ChronoUnit.NANOS);
		long localNanoInterval3 = START_DATE_TIME_INITIAL_POINT.until(LocalDateTime.now(), ChronoUnit.NANOS);
		LOGGER.info("localNanoInterval  [{}] ", localNanoInterval);
		LOGGER.info("localNanoInterval2 [{}] ", localNanoInterval2);
		LOGGER.info("localNanoInterval3 [{}] ", localNanoInterval3);

		LocalDateTime ld2[] = new LocalDateTime[512];
		ld2[0] = LocalDateTime.now();
		int i = 0;
		do {
			i++;
			ld2[i] = LocalDateTime.now();
		} while (ld2[i].equals(ld2[i - 1]));
		LOGGER.info("localNanoNow {}     [{}] ", i - 1, ld2[i - 1]);
		LOGGER.info("localNanoNow {}     [{}] ", i, ld2[i]);

	}

}
