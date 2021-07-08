package edu.hm.cs;

import java.util.*;

public class IterationPerformanceTest {
    private static final int WARMUP_CYCLES = 100_000;
    private static final int MAX_TEST_SIZE = 100_000;
    private static final String FOR_LOOP = "For Loop";
    private static final String ENHANCED_FOR_LOOP = "Enhanced For Loop";
    private static final String ITERATOR_WITH_WHILE = "Iterator With While";
    private static final String ITERATOR_WITH_FOR_LOOP = "Iterator With For Loop";
    private static final String STREAM_FOR_EACH = "Stream For Each";
    private static final String PARALLEL_STREAM = "Parallel Stream";
    private static final int NUMBER_OF_TESTS = 1;
    private static final Random RANDOM = new Random();
    //private static final DisplayFrame DISPLAY_FRAME = new DisplayFrame();

    public static void main(String[] args) {
        warmup();

        testArray();
        testArrayList();
        testHashSet();
        testLinkedList();
    }

    private IterationPerformanceTest() {}

    private void doNothing() {}

    private static void warmup() {
        for (int i = 0; i < WARMUP_CYCLES; i++) {
            final IterationPerformanceTest dummy = new IterationPerformanceTest();
            dummy.doNothing();
        }
    }

    private static void initializeResultsMap(final Map<String, int[]> results, final String... testNames) {
        for (final String name : testNames) {
            results.put(name, new int[(int) Math.log10(MAX_TEST_SIZE)]);
        }
    }

    private static void testArray() {
        int[] array;
        final Map<String, int[]> results = new LinkedHashMap<>();
        initializeResultsMap(results, FOR_LOOP, ENHANCED_FOR_LOOP, STREAM_FOR_EACH, PARALLEL_STREAM);
        System.out.println("Array");

        for (int size = 10; size <= MAX_TEST_SIZE; size*=10) {
            array = RANDOM.ints(size).toArray();
            long sumOfResults = 0;


            for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
                //test code
                sumOfResults += testForLoop(array);
            }
            results.get(FOR_LOOP)[(int) (Math.log10(size)-1)] = (int) sumOfResults/NUMBER_OF_TESTS;
            sumOfResults = 0;


            for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
                //test code
                sumOfResults += testEnhancedFor(array);
            }
            results.get(ENHANCED_FOR_LOOP)[(int) (Math.log10(size)-1)] = (int) sumOfResults/NUMBER_OF_TESTS;
            sumOfResults = 0;


            for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
                //test code
                sumOfResults += testStreamForEach(array);
            }
            results.get(STREAM_FOR_EACH)[(int) (Math.log10(size)-1)] = (int) sumOfResults/NUMBER_OF_TESTS;
            sumOfResults = 0;


            for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
                //test code
                sumOfResults += testParallelStream(array);
            }
            results.get(PARALLEL_STREAM)[(int) (Math.log10(size)-1)] = (int) sumOfResults/NUMBER_OF_TESTS;
            sumOfResults = 0;
        }
        //DISPLAY_FRAME.drawResults("Array", results);
        printResults(results);
    }

    private static void testArrayList() {
        final List<Integer> arrayList = new ArrayList<>();
        final Map<String, int[]> results = new LinkedHashMap<>();
        initializeResultsMap(results, FOR_LOOP, ENHANCED_FOR_LOOP, ITERATOR_WITH_WHILE, ITERATOR_WITH_FOR_LOOP, STREAM_FOR_EACH, PARALLEL_STREAM);
        System.out.println("ArrayList");

        for (int size = 10; size <= MAX_TEST_SIZE; size*=10) {
            for (int i = arrayList.size(); i < size; i++) {
                arrayList.add(RANDOM.nextInt());
            }
            long sumOfResults = 0;

            for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
                //test code
                sumOfResults += testForLoop(arrayList);
            }
            results.get(FOR_LOOP)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);

            testCollection(arrayList, results);
        }

        //DISPLAY_FRAME.drawResults("ArrayList", results);
        printResults(results);
    }

    private static void testHashSet() {
        final Set<Integer> hashSet = new HashSet<>();
        final Map<String, int[]> results = new LinkedHashMap<>();
        initializeResultsMap(results, ENHANCED_FOR_LOOP, ITERATOR_WITH_WHILE, ITERATOR_WITH_FOR_LOOP, STREAM_FOR_EACH, PARALLEL_STREAM);
        System.out.println("HashSet");

        for (int size = 10; size <= MAX_TEST_SIZE; size*=10) {
            for (int i = hashSet.size(); i < size; i++) {
                hashSet.add(RANDOM.nextInt());
            }
            //doesn't have method to get by index

            testCollection(hashSet, results);
        }

        //DISPLAY_FRAME.drawResults("HashSet", results);
        printResults(results);
    }

    private static void testLinkedList() {
        final List<Integer> linkedList = new LinkedList<>();
        final Map<String, int[]> results = new LinkedHashMap<>();
        initializeResultsMap(results, FOR_LOOP, ENHANCED_FOR_LOOP, ITERATOR_WITH_WHILE, ITERATOR_WITH_FOR_LOOP, STREAM_FOR_EACH, PARALLEL_STREAM);
        System.out.println("LinkedList");

        for (int size = 10; size <= MAX_TEST_SIZE; size*=10) {
            for (int i = linkedList.size(); i < size; i++) {
                linkedList.add(RANDOM.nextInt());
            }
            long sumOfResults = 0;

            for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
                //test code
                sumOfResults += testForLoop(linkedList);
            }
            results.get(FOR_LOOP)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);

            testCollection(linkedList, results);
        }

        //DISPLAY_FRAME.drawResults("LinkedList", results);
        printResults(results);
    }

    private static void testCollection(final Collection<Integer> collection, final Map<String, int[]> results) {
        long sumOfResults = 0;
        final int size = collection.size();

        //System.out.println(ENHANCED_FOR_LOOP);
        for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
            //test code
            sumOfResults += testEnhancedFor(collection);
        }
        results.get(ENHANCED_FOR_LOOP)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);
        sumOfResults = 0;


        //System.out.println(ITERATOR_WITH_WHILE);
        for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
            //test code
            sumOfResults += testIteratorWithWhile(collection);
        }
        results.get(ITERATOR_WITH_WHILE)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);
        sumOfResults = 0;


        //System.out.println(ITERATOR_WITH_FOR_LOOP);
        for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
            //test code
            sumOfResults += testIteratorWithFor(collection);
        }
        results.get(ITERATOR_WITH_FOR_LOOP)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);
        sumOfResults = 0;


        //System.out.println(STREAM_FOR_EACH);
        for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
            //test code
            sumOfResults += testStreamForEach(collection);
        }
        results.get(STREAM_FOR_EACH)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);
        sumOfResults = 0;


        //System.out.println(PARALLEL_STREAM);
        for (int testNumber = 0; testNumber < NUMBER_OF_TESTS; testNumber++) {
            //test code
            sumOfResults += testParallelStream(collection);
        }
        results.get(PARALLEL_STREAM)[(int) (Math.log10(size)-1)] = (int) (sumOfResults/NUMBER_OF_TESTS);
    }

    public static void printResults(final Map<String, int[]> results) {
        for (final String testType : results.keySet()) {
            System.out.println(testType + ":\n" + Arrays.toString(results.get(testType)));
        }
        System.out.println();
    }

    private static long testForLoop(final int[] array) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        for (int i = 0; i < array.length; i++) {
            final int temp = array[i];
        }
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testEnhancedFor(final int[] array) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        for (final int temp : array) {

        }
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testStreamForEach(final int[] array) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        Arrays.stream(array).forEach((element) -> {
            final int temp = element;
        });
        time2 = System.nanoTime();
        return  time2 - time1;
    }

    private static long testParallelStream(final int[] array) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        Arrays.stream(array).parallel().forEach((element) -> {
            final int temp = element;
        });
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testForLoop(final List<Integer> list) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        for (int i = 0; i < list.size(); i++) {
            final int temp = list.get(i);
        }
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testEnhancedFor(final Collection<Integer> collection) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        for (final int temp : collection) {

        }
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testIteratorWithWhile(final Collection<Integer> collection) {
        long time1;
        long time2;

        final Iterator<Integer> iterator = collection.iterator();
        time1 = System.nanoTime();
        while (iterator.hasNext()) {
            final int temp = iterator.next();
        }
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testIteratorWithFor(final Collection<Integer> collection) {
        long time1;
        long time2;

        final Iterator<Integer> iterator = collection.iterator();
        time1 = System.nanoTime();
        for (int i = 0; i < collection.size(); i++) {
            final int temp = iterator.next();
        }
        time2 = System.nanoTime();
        return time2 - time1;
    }

    private static long testStreamForEach(final Collection<Integer> collection) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        collection.stream().forEach((element) -> {
            final int temp = element;
        });
        time2 = System.nanoTime();
        return  time2 - time1;
    }

    private static long testParallelStream(final Collection<Integer> collection) {
        long time1;
        long time2;

        time1 = System.nanoTime();
        collection.stream().parallel().forEach((element) -> {
            final int temp = element;
        });
        time2 = System.nanoTime();
        return time2 - time1;
    }
}
