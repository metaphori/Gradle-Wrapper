package it.unibo.iot;

import com.google.common.collect.Streams;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class ProducerConsumerAssertions {
    public static void assertOrderedConsumptionOnLog(List<String> lines){
        List<String> producedStuff = lines.stream()
                .filter(s -> s.contains("Produce"))
                .map(s -> s.substring(s.lastIndexOf(" ")))
                .collect(Collectors.toList());
        List<String> consumedStuff = lines.stream()
                .filter(s -> s.contains("Consume"))
                .map(s -> s.substring(s.lastIndexOf(" ")))
                .collect(Collectors.toList());

        Assert.assertTrue(producedStuff.size()>0);
        Assert.assertTrue(producedStuff.size()==consumedStuff.size());
        Assert.assertTrue(Streams.zip(producedStuff.stream(), consumedStuff.stream(), (a, b) -> a.equals(b)).allMatch(x -> x));
    }
}
