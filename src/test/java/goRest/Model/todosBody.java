package goRest.Model;

import java.util.List;

public class todosBody {
    private Meta meta;
    private List<Todos> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Todos> getData() {
        return data;
    }

    public void setData(List<Todos> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "todosBody{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }
}
