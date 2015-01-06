package com.hp.maas.usecases.metadataExport;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.metadata.RelationDescriptor;
import com.hp.maas.usecases.workflow.WorkflowRulePath;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 11/12/2014.
 */
public class CreateMetadataCSVs {

    private Server server;
    private File outputFolder;

    public CreateMetadataCSVs(Server server, File outputFolder) {
        this.server = server;
        this.outputFolder = outputFolder;
    }

    public void run(){
        List<EntityTypeDescriptor> entityTypeDescriptors = server.getMetadataAPI().loadAllFromServer();

        for (EntityTypeDescriptor md : entityTypeDescriptors) {
            dumpCsv(md);
        }

    }

    private void dumpCsv(EntityTypeDescriptor md) {
        StringBuilder csv = new StringBuilder();
        CSVPrinter printer;
        try {
            printer = new CSVPrinter(csv, CSVFormat.EXCEL.withRecordSeparator("\n").withDelimiter(','));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            printer.printRecord("name",
                    "logicalType",
                    "localizedLabelKey",
                    "domain",
                    "hidden",
                    "reference",
                    "system",
                    "searchable",
                    "sortable",
                    "textSearchable",
                    "required",
                    "readOnly",
                    "unique");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (FieldDescriptor f : md.getFields()) {
            try {
                printer.printRecord(f.getName(),
                        f.getLogicalType(),
                        f.getLocalizedLabelKey(),
                        f.getDomain(),
                        f.isHidden(),
                        f.getReference() == null? "" : f.getReference().getTargetType(),
                        f.isSystem(),
                        f.isSearchable(),
                        f.isSortable(),
                        f.isTextSearchable(),
                        f.isRequired(),
                        f.isReadOnly(),
                        f.isUnique());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (RelationDescriptor r : md.getRelations()) {
            if (!"MANY2MANY".equals(r.getCardinality())){
                continue;
            }
            /*try {
                printer.printRecord(r.getName(),
                        r.get,
                        f.getLocalizedLabelKey(),
                        f.getDomain(),
                        f.isHidden(),
                        r.,
                        f.isSystem(),
                        f.isSearchable(),
                        f.isSortable(),
                        f.isTextSearchable(),
                        f.isRequired(),
                        f.isReadOnly(),
                        f.isUnique());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        }

        try {
            FileUtils.writeStringToFile(new File(outputFolder.getAbsolutePath()+File.separator+md.getName()+".csv"),csv.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
