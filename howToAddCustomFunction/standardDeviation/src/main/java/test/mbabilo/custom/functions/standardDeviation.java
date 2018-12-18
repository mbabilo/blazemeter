package test.mbabilo.custom.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class standardDeviation extends AbstractFunction {


    private static final String MyFunctionName = "__standardDeviation-Sample";
    private static final String SEPARATOR = "\\|";

    private static final List < String > desc = new LinkedList < String > ();
    static {
        desc.add("Array of numbers separated by \"|\", fractional numbers through point.");
        desc.add("Name of variable in which to store the result (optional)");
    }
    private Object[] temp;

    public String execute(SampleResult arg0, Sampler arg1) throws InvalidVariableException {

        JMeterVariables vars = getVariables();

        final String originalString = ((CompoundVariable) temp[0]).execute().trim();

        String[] numbers = originalString.split(SEPARATOR);
        double[] input = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            input[i] = Double.valueOf(numbers[i]);
        }
        double dv = populationStandardDeviation(input);

//user might want the result in a variable
        if (null != vars) {
            String resultVariable = ((CompoundVariable) temp[1]).execute().trim();
            vars.put(resultVariable, String.valueOf(dv)); //store the result in the user defined variable
        }
        return String.valueOf(dv);
    }

    private strictfp double populationStandardDeviation(double[] temp) {
        double mean = mean(temp);
        double n = temp.length;
        double dv = 0;
        for (double d : temp) {
            double dm = d - mean;
            dv += dm * dm;
        }
        return Math.sqrt(dv / (n - 1));
    }

    private strictfp double mean(double[] temp) {
        return sum(temp) / temp.length;
    }

    private strictfp double sum(double[] temp) {
        if (temp == null || temp.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }
        else {
            double sum = 0;
            for (double var : temp) {
                sum += var;
            }
            return sum;
        }
    }

    public void setParameters(Collection < CompoundVariable > parameters) throws InvalidVariableException {
        temp = parameters.toArray();
    }

    public String getReferenceKey() {
        return MyFunctionName;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }
}