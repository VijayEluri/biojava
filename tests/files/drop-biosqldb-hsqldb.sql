-- For some reason we need to drop the constraints explicitly
-- There's probably a correct way to do this.
ALTER TABLE term DROP CONSTRAINT FKont_term;
ALTER TABLE term_synonym DROP CONSTRAINT FKterm_syn;
ALTER TABLE term_dbxref DROP CONSTRAINT FKdbxref_trmdbxref;
ALTER TABLE term_dbxref DROP CONSTRAINT FKterm_trmdbxref;
ALTER TABLE term_relationship DROP CONSTRAINT FKtrmsubject_trmrel;
ALTER TABLE term_relationship DROP CONSTRAINT FKtrmpredicate_trmrel;
ALTER TABLE term_relationship DROP CONSTRAINT FKtrmobject_trmrel;
ALTER TABLE term_relationship DROP CONSTRAINT FKterm_trmrel;
ALTER TABLE term_path DROP CONSTRAINT FKtrmsubject_trmpath;
ALTER TABLE term_path DROP CONSTRAINT FKtrmpredicate_trmpath;
ALTER TABLE term_path DROP CONSTRAINT FKtrmobject_trmpath;
ALTER TABLE term_path DROP CONSTRAINT FKontology_trmpath;
-- ALTER TABLE taxon DROP CONSTRAINT FKtaxon_taxon;
ALTER TABLE taxon_name DROP CONSTRAINT FKtaxon_taxonname;
ALTER TABLE bioentry DROP CONSTRAINT FKtaxon_bioentry;
ALTER TABLE bioentry DROP CONSTRAINT FKbiodatabase_bioentry;
ALTER TABLE bioentry_relationship DROP CONSTRAINT FKterm_bioentryrel;
ALTER TABLE bioentry_relationship DROP CONSTRAINT FKparentent_bioentryrel;
ALTER TABLE bioentry_relationship DROP CONSTRAINT FKchildent_bioentryrel;
ALTER TABLE bioentry_path DROP CONSTRAINT FKterm_bioentrypath;
ALTER TABLE bioentry_path DROP CONSTRAINT FKparentent_bioentrypath;
ALTER TABLE bioentry_path DROP CONSTRAINT FKchildent_bioentrypath;
ALTER TABLE biosequence DROP CONSTRAINT FKbioentry_bioseq;
ALTER TABLE anncomment DROP CONSTRAINT FKbioentry_comment;
ALTER TABLE bioentry_dbxref DROP CONSTRAINT FKbioentry_dblink;
ALTER TABLE bioentry_dbxref DROP CONSTRAINT FKdbxref_dblink;
ALTER TABLE dbxref_qualifier_value DROP CONSTRAINT FKtrm_dbxrefqual;
ALTER TABLE dbxref_qualifier_value DROP CONSTRAINT FKdbxref_dbxrefqual;
ALTER TABLE bioentry_reference DROP CONSTRAINT FKbioentry_entryref;
ALTER TABLE bioentry_reference DROP CONSTRAINT FKreference_entryref;
ALTER TABLE bioentry_qualifier_value DROP CONSTRAINT FKbioentry_entqual;
ALTER TABLE bioentry_qualifier_value DROP CONSTRAINT FKterm_entqual;
ALTER TABLE reference DROP CONSTRAINT FKdbxref_reference;
ALTER TABLE seqfeature DROP CONSTRAINT FKterm_seqfeature;
ALTER TABLE seqfeature DROP CONSTRAINT FKsourceterm_seqfeature;
ALTER TABLE seqfeature DROP CONSTRAINT FKbioentry_seqfeature;
ALTER TABLE seqfeature_relationship DROP CONSTRAINT FKterm_seqfeatrel;
ALTER TABLE seqfeature_relationship DROP CONSTRAINT FKparentfeat_seqfeatrel;
ALTER TABLE seqfeature_relationship DROP CONSTRAINT FKchildfeat_seqfeatrel;
ALTER TABLE seqfeature_path DROP CONSTRAINT FKterm_seqfeatpath;
ALTER TABLE seqfeature_path DROP CONSTRAINT FKparentfeat_seqfeatpath;
ALTER TABLE seqfeature_path DROP CONSTRAINT FKchildfeat_seqfeatpath;
ALTER TABLE seqfeature_qualifier_value DROP CONSTRAINT FKterm_featqual;
ALTER TABLE seqfeature_qualifier_value DROP CONSTRAINT FKseqfeature_featqual;
ALTER TABLE seqfeature_dbxref DROP CONSTRAINT FKseqfeature_feadblink;
ALTER TABLE seqfeature_dbxref DROP CONSTRAINT FKdbxref_feadblink;
ALTER TABLE location DROP CONSTRAINT FKseqfeature_location;
ALTER TABLE location DROP CONSTRAINT FKdbxref_location;
ALTER TABLE location DROP CONSTRAINT FKterm_featloc;
ALTER TABLE location_qualifier_value DROP CONSTRAINT FKfeatloc_locqual;
ALTER TABLE location_qualifier_value DROP CONSTRAINT FKterm_locqual;


DROP TABLE anncomment;
DROP TABLE biodatabase;
DROP TABLE bioentry;
DROP TABLE bioentry_dbxref;
DROP TABLE bioentry_path;
DROP TABLE bioentry_qualifier_value;
DROP TABLE bioentry_reference;
DROP TABLE bioentry_relationship;
DROP TABLE biosequence; 
DROP TABLE dbxref;
DROP TABLE dbxref_qualifier_value;
DROP TABLE location;
DROP TABLE location_qualifier_value;
DROP TABLE ontology;
DROP TABLE reference;
DROP TABLE seqfeature;
DROP TABLE seqfeature_dbxref;
DROP TABLE seqfeature_path;
DROP TABLE seqfeature_qualifier_value;
DROP TABLE seqfeature_relationship;
DROP TABLE taxon;
DROP TABLE taxon_name;
DROP TABLE term;
DROP TABLE term_dbxref;
DROP TABLE term_path;
DROP TABLE term_relationship;
DROP TABLE term_synonym;


-- Hsqldb complains about indexes not existing -- maybe it automatically
-- deletes them when you remove the table?
--DROP INDEX bioentrypath_parent ON bioentry_pathobject_bioentry_id);
--DROP INDEX bioentryrel_parent;
--DROP INDEX ontrel_subjectid ON term_relationshipsubject_term_id);
--DROP INDEX seqfeature_bioentryid ON seqfeaturebioentry_id);
--DROP INDEX seqfeaturerel_parent ON seqfeature_pathobject_seqfeature_id);
--DROP INDEX seqfeaturerel_parent ON seqfeature_relationshipobject_seqfeature_id);
--DROP INDEX trmpath_subjectid ON term_pathsubject_term_id);
-- DROP INDEX bioentry_db;
-- DROP INDEX bioentry_name;
-- DROP INDEX bioentry_tax;
-- DROP INDEX bioentrypath_child;
-- DROP INDEX bioentrypath_trm;
-- DROP INDEX bioentryqual_trm;
-- DROP INDEX bioentryref_ref;
-- DROP INDEX bioentryrel_child;
-- DROP INDEX bioentryrel_trm;
-- DROP INDEX db_auth;
-- DROP INDEX dblink_dbx;
-- DROP INDEX dbxref_db;
-- DROP INDEX dbxrefqual_dbx;
-- DROP INDEX dbxrefqual_trm;
-- DROP INDEX feadblink_dbx;
-- DROP INDEX locationqual_trm;
-- DROP INDEX seqfeature_fsrc;
-- DROP INDEX seqfeature_trm;
-- DROP INDEX seqfeatureloc_dbx;
-- DROP INDEX seqfeatureloc_start;
-- DROP INDEX seqfeatureloc_trm;
-- DROP INDEX seqfeaturepath_child;
-- DROP INDEX seqfeaturepath_trm;
-- DROP INDEX seqfeaturequal_trm;
-- DROP INDEX seqfeaturerel_child;
-- DROP INDEX seqfeaturerel_trm;
-- DROP INDEX taxnamename;
-- DROP INDEX taxnametaxonid;
-- DROP INDEX taxparent;
-- DROP INDEX term_ont;
-- DROP INDEX trmdbxref_dbxrefid;
-- DROP INDEX trmpath_objectid;
-- DROP INDEX trmpath_ontid;
-- DROP INDEX trmpath_predicateid;
-- DROP INDEX trmrel_objectid;
-- DROP INDEX trmrel_ontid;
-- DROP INDEX trmrel_predicateid;

