package xyz.froud.jmccul.counter.LS7266R1;

import xyz.froud.jmccul.JMCCULException;
import xyz.froud.jmccul.JMCCULUtils;
import xyz.froud.jmccul.jna.MeasurementComputingUniversalLibrary;

/**
 *
 * @author Peter Froud
 */
public class LS7266R1 {

    private final int BOARD_NUMBER = 0;

    public void config7266(int coutnerNumber, Quadrature quadrature, CountingMode countingMode, Encoding dataEncoding, IndexMode indexMode, boolean invertIndex, FlagPins flagPins, boolean gating) throws JMCCULException {

        final int errorCode = MeasurementComputingUniversalLibrary.INSTANCE.cbC7266Config(
                BOARD_NUMBER,
                coutnerNumber,
                quadrature.VALUE,
                countingMode.VALUE,
                dataEncoding.VALUE,
                indexMode.VALUE,
                (invertIndex ? MeasurementComputingUniversalLibrary.ENABLED : MeasurementComputingUniversalLibrary.DISABLED),
                flagPins.VALUE,
                (gating ? MeasurementComputingUniversalLibrary.ENABLED : MeasurementComputingUniversalLibrary.DISABLED)
        );
        JMCCULUtils.checkError(errorCode);


    }

}
