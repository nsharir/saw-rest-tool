package com.hp.maas.usecases.forms;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.jsons.forms.Form;
import com.hp.maas.jsons.forms.FormField;
import com.hp.maas.jsons.forms.FormParser;
import com.hp.maas.jsons.forms.FormSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sharir on 27/11/2014.
 */
public class FormsValidationFinder {

    private Server server;

    public FormsValidationFinder(Server server) {
        this.server = server;
    }

    public void run(){

        report("** Validating tenant "+server.getTenantId()+"...");

        server.authenticate();

        List<RMSInstance> rmsInstances = server.getRmsReaderAPI().readResources("formLayout");

        List<Form> forms = new ArrayList<Form>();

        for (RMSInstance rmsInstance : rmsInstances) {
            forms.add(FormParser.toForm(rmsInstance.getContent()));
        }

        int invalid = 0;

        for (Form form : forms) {
            boolean b = validateForm(form);
            if (!b){
                invalid++;
            }
        }

        if (invalid == 0) {
            report("** Done validating tenant " + server.getTenantId() + " - all "+forms.size()+" forms are valid.");
        }else{
            report("** Done validating tenant " + server.getTenantId()+" - " +invalid+ " out of "+forms.size()+" forms failed on errors.");
        }

    }

    private boolean validateForm(Form form) {

        List<String> errors = new ArrayList<String>();

        EntityTypeDescriptor descriptor = null;

        try{
            descriptor = server.getMetadataAPI().getEntityDescriptor(form.getEntityType());
        }catch (Exception e){
            //do nothing.
        }

        if (descriptor == null){
            errors.add("["+form.getEntityType()+"] is not a valid entity type");
        }else {

            for (FormSection section : form.getSections()) {
                HashSet<String> badField = new HashSet<String>();

                if (section.getName() == null){
                    errors.add("["+(section.getHeader() != null ? section.getHeader(): section.getName())+"] section has no [name] property." );
                }

                if (section.getResourceKey() == null){
                    errors.add("["+(section.getHeader() != null ? section.getHeader(): section.getName())+"] section has no [resourceKey] property." );
                }

                if (section.getDomain() == null){
                    errors.add("["+(section.getHeader() != null ? section.getHeader(): section.getName())+"] section has no [domain] property." );
                }

                for (FormField field : section.getFields()) {

                    if (field.getEditorType().equals("MORE")){
                        continue;
                    }
                    FieldDescriptor md = descriptor.getField(field.getModelAttribute());
                    if (md == null) {
                        if (descriptor.getRelation(field.getModelAttribute()) == null) {
                            errors.add("[" + field.getModelAttribute() + "] doesn't exist in metadata of entity type " + form.getEntityType() + " (section = " + section.getName() + ")");
                            badField.add(field.getModelAttribute());
                        }
                    }else{
                        if (md.isHidden()){
                            errors.add("[" + field.getModelAttribute() + "] is define as hidden field in entity type " + form.getEntityType() + " (section = " + section.getName() + ")");
                            badField.add(field.getModelAttribute());
                        }
                    }
                }

                if (section.getFields().isEmpty() || ((section.getFields().size() - badField.size()) == 0)) {
                    errors.add("["+section.getName()+"] Section has no fields (or all of his fields are invalid)");
                }
            }
        }


        if (errors.isEmpty()){
            //report("  Form ["+form.getName()+"]" +" [entityType="+form.getEntityType()+"] was verified successfully." );
        }
        else{
            reportError("Form ["+form.getName()+"]" +" [entityType="+form.getEntityType() + "] failed on some validations (" + errors.size() + "):");
            for (String error : errors) {
                reportError("    - "+error);
            }
        }

        return errors.isEmpty();

    }


    private void report(String text){
        System.out.println(text);
    }

    private void reportError(String text){
        System.out.println(" ["+server.getTenantId()+"] "+text);
    }

}
