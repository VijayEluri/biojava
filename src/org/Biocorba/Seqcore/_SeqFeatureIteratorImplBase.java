/*
 * File: ..\SRC/ORG/BIOCORBA/SEQCORE/_SEQFEATUREITERATORIMPLBASE.JAVA
 * From: BIOCORBA.11-02-2000.IDL
 * Date: Fri Feb 11 14:53:21 2000
 *   By: idltojava Java IDL 1.2 Aug 18 1998 16:25:34
 */

package org.Biocorba.Seqcore;
public abstract class _SeqFeatureIteratorImplBase extends org.omg.CORBA.DynamicImplementation implements org.Biocorba.Seqcore.SeqFeatureIterator {
    // Constructor
    public _SeqFeatureIteratorImplBase() {
         super();
    }
    // Type strings for this class and its superclases
    private static final String _type_ids[] = {
        "IDL:org/Biocorba/Seqcore/SeqFeatureIterator:1.0",
        "IDL:GNOME/Unknown:1.0"
    };

    public String[] _ids() { return (String[]) _type_ids.clone(); }

    private static java.util.Dictionary _methods = new java.util.Hashtable();
    static {
      _methods.put("ref", new java.lang.Integer(0));
      _methods.put("unref", new java.lang.Integer(1));
      _methods.put("query_interface", new java.lang.Integer(2));
      _methods.put("next", new java.lang.Integer(3));
      _methods.put("has_more", new java.lang.Integer(4));
     }
    // DSI Dispatch call
    public void invoke(org.omg.CORBA.ServerRequest r) {
       switch (((java.lang.Integer) _methods.get(r.op_name())).intValue()) {
           case 0: // GNOME.Unknown.ref
              {
              org.omg.CORBA.NVList _list = _orb().create_list(0);
              r.params(_list);
                            this.ref();
              org.omg.CORBA.Any __return = _orb().create_any();
              __return.type(_orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_void));
              r.result(__return);
              }
              break;
           case 1: // GNOME.Unknown.unref
              {
              org.omg.CORBA.NVList _list = _orb().create_list(0);
              r.params(_list);
                            this.unref();
              org.omg.CORBA.Any __return = _orb().create_any();
              __return.type(_orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_void));
              r.result(__return);
              }
              break;
           case 2: // GNOME.Unknown.query_interface
              {
              org.omg.CORBA.NVList _list = _orb().create_list(0);
              org.omg.CORBA.Any _repoid = _orb().create_any();
              _repoid.type(org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_string));
              _list.add_value("repoid", _repoid, org.omg.CORBA.ARG_IN.value);
              r.params(_list);
              String repoid;
              repoid = _repoid.extract_string();
              org.omg.CORBA.Object ___result;
                            ___result = this.query_interface(repoid);
              org.omg.CORBA.Any __result = _orb().create_any();
              __result.insert_Object(___result);
              r.result(__result);
              }
              break;
           case 3: // org.Biocorba.Seqcore.SeqFeatureIterator.next
              {
              org.omg.CORBA.NVList _list = _orb().create_list(0);
              r.params(_list);
              org.Biocorba.Seqcore.SeqFeature ___result;
              try {
                            ___result = this.next();
              }
              catch (org.Biocorba.Seqcore.EndOfStream e0) {
                            org.omg.CORBA.Any _except = _orb().create_any();
                            org.Biocorba.Seqcore.EndOfStreamHelper.insert(_except, e0);
                            r.except(_except);
                            return;
              }
              catch (org.Biocorba.Seqcore.UnableToProcess e1) {
                            org.omg.CORBA.Any _except = _orb().create_any();
                            org.Biocorba.Seqcore.UnableToProcessHelper.insert(_except, e1);
                            r.except(_except);
                            return;
              }
              org.omg.CORBA.Any __result = _orb().create_any();
              org.Biocorba.Seqcore.SeqFeatureHelper.insert(__result, ___result);
              r.result(__result);
              }
              break;
           case 4: // org.Biocorba.Seqcore.SeqFeatureIterator.has_more
              {
              org.omg.CORBA.NVList _list = _orb().create_list(0);
              r.params(_list);
              boolean ___result;
                            ___result = this.has_more();
              org.omg.CORBA.Any __result = _orb().create_any();
              __result.insert_boolean(___result);
              r.result(__result);
              }
              break;
            default:
              throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
       }
 }
}
