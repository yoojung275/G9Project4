package org.g9project4.board.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardData is a Querydsl query type for BoardData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardData extends EntityPathBase<BoardData> {

    private static final long serialVersionUID = -538996862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardData boardData = new QBoardData("boardData");

    public final org.g9project4.global.entities.QBaseEntity _super = new org.g9project4.global.entities.QBaseEntity(this);

    public final QBoard board;

    public final StringPath category = createString("category");

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final BooleanPath editorView = createBoolean("editorView");

    public final StringPath gid = createString("gid");

    public final StringPath guestPw = createString("guestPw");

    public final StringPath ip = createString("ip");

    public final StringPath longText1 = createString("longText1");

    public final StringPath longText2 = createString("longText2");

    public final org.g9project4.member.entities.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final BooleanPath notice = createBoolean("notice");

    public final NumberPath<Long> num1 = createNumber("num1", Long.class);

    public final NumberPath<Long> num2 = createNumber("num2", Long.class);

    public final NumberPath<Long> num3 = createNumber("num3", Long.class);

    public final StringPath poster = createString("poster");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath subject = createString("subject");

    public final StringPath text1 = createString("text1");

    public final StringPath text2 = createString("text2");

    public final StringPath text3 = createString("text3");

    public final StringPath ua = createString("ua");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QBoardData(String variable) {
        this(BoardData.class, forVariable(variable), INITS);
    }

    public QBoardData(Path<? extends BoardData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardData(PathMetadata metadata, PathInits inits) {
        this(BoardData.class, metadata, inits);
    }

    public QBoardData(Class<? extends BoardData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board")) : null;
        this.member = inits.isInitialized("member") ? new org.g9project4.member.entities.QMember(forProperty("member")) : null;
    }

}

