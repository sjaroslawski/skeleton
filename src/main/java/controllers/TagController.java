package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;


//import api.CreateTagRequest;
import dao.TagDao;
import generated.tables.records.TagsRecord;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @PUT
    @Path("/tags/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, @Valid int id) {
        if (tags.checkForTag(tagName, id)){
            tags.delete(tagName, id);
        }
        else{
            tags.insert(tagName, id);
        }
    }

    @GET
    @Path("/tags/{tag}")
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.getTagReceipts(tagName);
        List<TagsRecord> tagsRecords = tags.getAllTags();
        return receiptRecords.stream().map((receipt) -> new ReceiptResponse(receipt, tagsRecords.stream().filter((tag) -> tag.getId() == receipt.getId()).map((tag) -> tag.getTag()).collect(toList()))).collect(toList());
    }
}