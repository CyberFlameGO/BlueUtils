package net.axay.blueutils.miscellaneous;

import java.util.Objects;

/**
 * @deprecated Use kotlin Pair instead.
 */
@Deprecated
public class Pair<T, E> {

    private T first;
    private E second;

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(E second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair {" +
                "first = " + first +
                ", second = " + second +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

}