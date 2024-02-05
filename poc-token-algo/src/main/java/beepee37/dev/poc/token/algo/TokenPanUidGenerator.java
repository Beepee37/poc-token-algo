package beepee37.dev.poc.token.algo;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pan token generator.<br>
 * Based on the UUID principle<br>
 * see https://www.ietf.org/rfc/rfc4122.txt for some principles to applu=y<br>
 * 
 */
public class TokenPanUidGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenPanUidGenerator.class);
	private final byte paddingLetter;
	private final byte clockSequence;
	private final byte nodeId;
	private final ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();
	private final CyclicCounter cyclicCounter = new CyclicCounter(65536);

	private static final LocalDateTime START_DATE_TIME_INITIAL_POINT = LocalDateTime.of(2022, 12, 31, 0, 0);

	TokenPanUidGenerator(char paddingLetter, short clockSequence, short nodeId) {
		this.paddingLetter = (byte) paddingLetter;
		if (clockSequence > 255 || clockSequence < 0)
			throw new IllegalArgumentException("clockSequence must be positive and strictle less than 256");
		this.clockSequence = (ByteBuffer.wrap(new byte[2]).putShort(clockSequence).array())[1];
		;
		if (nodeId > 255 || nodeId < 0)
			throw new IllegalArgumentException("nodeId must be positive and strictle less than 256");
		this.nodeId = (ByteBuffer.wrap(new byte[2]).putShort(nodeId).array())[1];
	}

	public String GenerateTokenFromPan(String pan) throws DecoderException {

		byte[] uuid = new byte[12];// by default it is field with zeros.
		
		long localNanoInterval = START_DATE_TIME_INITIAL_POINT.until(LocalDateTime.now(), ChronoUnit.NANOS);
		byte[] localNanoIntervalHexa = ByteBuffer.wrap(new byte[8]).putLong(localNanoInterval).array();

		LOGGER.debug("localNanoInterval hexa value {}", Long.toHexString(localNanoInterval));
		int currentIndex = Long.BYTES;
		System.arraycopy(localNanoIntervalHexa, 0, uuid, currentIndex - localNanoIntervalHexa.length, localNanoIntervalHexa.length);
		uuid[currentIndex] = clockSequence;
		currentIndex++;
		uuid[currentIndex] = nodeId;
		currentIndex++;

		int randomint = randomGenerator.nextInt(65536);
		int cyclicSequenceNumber = cyclicCounter.incrementAndGet();
		LOGGER.debug("randomint : {}", randomint);
		LOGGER.debug("cyclicSequenceNumber : {}", cyclicSequenceNumber);

		byte[] randomIntAsBytes = ByteBuffer.wrap(new byte[4]).putInt(cyclicSequenceNumber).array();
		System.arraycopy(randomIntAsBytes, 2, uuid, currentIndex, randomIntAsBytes.length - 2);
		
		LOGGER.debug("uid hex value : {}", Hex.encodeHexString(uuid));

		byte[] encoded = Base64.getEncoder().encode(uuid);

		int panLength = pan.length();
		LOGGER.debug("UUID encoded B64 : {}", new String(encoded));

		int encodedLength = encoded.length;
		int paddingLenth = panLength - encodedLength;
		if (encodedLength < panLength) {
			byte[] paddedResult = new byte[panLength];
			for (int i = 0; i < paddingLenth; i++) {
				paddedResult[i] = this.paddingLetter;
			}
			for (int i = 0; i < encodedLength; i++) {
				paddedResult[i + paddingLenth] = encoded[i];
			}
			return new String(paddedResult);
		} else {
			return new String(encoded);
		}
	}

}
