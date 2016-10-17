package org.pg5100.backend.datalayer;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
        @NamedQuery(name = Comment.SUM_COMMENTS, query = "select count(c) from Comment c"),
})

@Entity
public class Comment extends Post {

    public static final String SUM_COMMENTS = "SUM_COMMENTS";

    boolean moderated;

    public Comment() {
        this(null, null);
    }

    public Comment(User author, String content) {
        super(author, null, content);
        moderated = false;
    }

    public void moderate() {
        moderated = !moderated;
    }
}
