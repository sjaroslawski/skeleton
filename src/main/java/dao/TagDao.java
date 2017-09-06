package dao;

import api.ReceiptResponse;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.TAGS;
import static generated.Tables.RECEIPTS;


public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tag, int id) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG, TAGS.ID)
                .values(tag, id)
                .returning(TAGS.ID)
                .fetchOne();
    }

    public boolean checkForTag(String currentTag, int currentID){
        return dsl.select().from(TAGS).where(TAGS.TAG.eq(currentTag).and(TAGS.ID.eq(currentID))).fetch().isNotEmpty();
    }

    public void delete(String givenTag, int givenId){
        dsl.delete(TAGS).where(TAGS.TAG.eq(givenTag).and(TAGS.ID.eq(givenId))).execute();
    }

    public List<ReceiptsRecord> getTagReceipts(String givenTag) {
        return dsl.select().from(RECEIPTS).join(TAGS).on(RECEIPTS.ID.eq(TAGS.ID)).where(TAGS.TAG.eq(givenTag)).fetchInto(ReceiptsRecord.class);
    }

}