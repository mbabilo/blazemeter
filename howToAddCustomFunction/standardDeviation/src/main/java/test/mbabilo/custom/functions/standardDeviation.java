package test.mbabilo.custom.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StandardDeviation extends AbstractFunction {


    private static final String MY_FUNCTION_NAME = "__standardDeviation-Sample";
    private static final String SEPARATOR1 = "\\|";
    private static final String SEPARATOR2 = ",";

    private static final List<String> DESC = Arrays.asList("Array of numbers separated by \"|\", fractional numbers through point.", "Name of variable in which to store the result (optional)");


    private Object[] temp;

    public String execute(SampleResult arg0, Sampler arg1) throws InvalidVariableException {

        JMeterVariables vars = getVariables();

        final String originalString = ((CompoundVariable) temp[0]).execute().trim();
        double[] input = new double[0];
        try {

            String[] numbers = split(originalString);
            input = new double[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                input[i] = Double.valueOf(numbers[i]);
            }
        } catch (Exception e) {
        }

        double dv = sampleStandardDeviation(input);

//user might want the result in a variable
        if (null != vars) {
            String resultVariable = ((CompoundVariable) temp[1]).execute().trim();
            vars.put(resultVariable, String.valueOf(dv)); //store the result in the user defined variable
        }
        return String.valueOf(dv);
    }

    private String[] split(String originalString) {
        String[] res = originalString.split(SEPARATOR1);
        if (res.length < 2) {
            res = originalString.split(SEPARATOR2);
        }
        return res;
    }

    private strictfp double sampleStandardDeviation(double[] temp) {
        double mean = mean(temp);
        double n = temp.length;
        double dv = 0;
        for (double d : temp) {
            double dm = d - mean;
            dv += dm * dm;
        }
        if (n < 2) {
            return 0;
        }
        return Math.sqrt(dv / (n - 1));
    }

    private strictfp double mean(double[] temp) {
        return sum(temp) / temp.length;
    }

    private strictfp double sum(double[] temp) {
        if (temp == null || temp.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        } else {
            double sum = 0;
            for (double var : temp) {
                sum += var;
            }
            return sum;
        }
    }

    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        temp = parameters.toArray();
    }

    public String getReferenceKey() {
        return MY_FUNCTION_NAME;
    }

    public List<String> getArgumentDesc() {
        return DESC;
    }
}