package ca.cmpt213.asn5.client;

import java.util.ArrayList;
import java.util.List;

public class JsonProcessor {
    private final List<String> names = new ArrayList();
    private final List<Double> weights = new ArrayList<>();
    private final List<Double> heights = new ArrayList<>();
    private final List<String> colours = new ArrayList<>();
    private final List<Long> tids = new ArrayList<>();
    public List<TokimonDisplay> objects = new ArrayList<>();

    public void processJsonString(String s) {
        for(int i = 0; i < s.length(); i++) {
            int firstIdxName;
            if(s.charAt(i) == 'n') {
                if(s.charAt(i + 1) == 'a') {
                    if(s.charAt(i + 2) == 'm') {
                        if(s.charAt(i + 3) == 'e') {
                            firstIdxName = i + 7;
                            wordField(names, s, i, firstIdxName);
                        }
                    }
                }
            }
        }

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == 'w') {
                numericalField(weights, s, i);
            }
        }

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == 'h') {
                numericalField(heights, s, i);
            }
        }

        for(int i = 0; i < s.length(); i++) {
            int firstIdxName;
            if(s.charAt(i) == 'c') {
                if(s.charAt(i + 1) == 'o') {
                    if(s.charAt(i + 2) == 'l') {
                        if(s.charAt(i + 3) == 'o') {
                            if(s.charAt(i + 4) == 'u') {
                                if(s.charAt(i + 5) == 'r') {
                                    firstIdxName = i + 9;
                                    wordField(colours, s, i, firstIdxName);
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < s.length(); i++) {
            int firstIdx;
            int secondIdx;
            String sub;
            if(s.charAt(i) == 't') {
                if(s.charAt(i + 1) == 'i') {
                    if(s.charAt(i + 2) == 'd') {
                        firstIdx = i + 5;
                        for(int j = i; j < s.length(); j++) {
                            if(s.charAt(j) == '}') {
                                secondIdx = j;
                                sub = s.substring(firstIdx,secondIdx);
                                Long convert = Long.parseLong(sub);
                                tids.add(convert);
                                break;
                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < heights.size(); i++) {
            TokimonDisplay displayObject = new TokimonDisplay(names.get(i),weights.get(i),heights.get(i),colours.get(i),tids.get(i));
            objects.add(displayObject);
        }
    }

    private static void wordField(List<String> colours, String s, int i, int firstIdxName) {
        int secondIdxName;
        String subName;
        for(int j = i; j < s.length(); j++) {
            if(s.charAt(j) == ',') {
                secondIdxName = j - 1;
                subName = s.substring(firstIdxName,secondIdxName);
                colours.add(subName);
                break;
            }
        }
    }

    private static void numericalField(List<Double> numericalFields, String s, int i) {
        int firstIdxName;
        int secondIdxName;
        String subName;
        if(s.charAt(i + 1) == 'e') {
            if(s.charAt(i + 2) == 'i') {
                if(s.charAt(i + 3) == 'g') {
                    if(s.charAt(i + 4) == 'h') {
                        if(s.charAt(i + 5) == 't') {
                            firstIdxName = i + 8;
                            for(int j = i; j < s.length(); j++) {
                                if(s.charAt(j) == ',') {
                                    secondIdxName = j;
                                    subName = s.substring(firstIdxName,secondIdxName);
                                    Double convert = Double.parseDouble(subName);
                                    numericalFields.add(convert);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
