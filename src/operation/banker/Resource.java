package operation.banker;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Billin on 2016/11/18.
 */
public class Resource implements Cloneable {

    int[] r;

    public Resource(int[] r) {
        this.r = r.clone();
    }

    void minus(Resource another) {
        IntStream.range(0, r.length).forEach(i -> r[i] = r[i] - another.r[i]);
    }

    void add(Resource another) {
        IntStream.range(0, r.length).forEach(i -> r[i] = r[i] + another.r[i]);
    }

    boolean isContain(Resource resource) {
        return IntStream.range(0, r.length).allMatch(i -> r[i] >= resource.r[i]);
    }

    @Override
    public Object clone() {
        Resource resource = null;
        try {
            resource = (Resource) super.clone();
            resource.r = r.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return resource;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "r=" + Arrays.toString(r) +
                '}';
    }
}
