import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class Stage {
    public int stage = 0;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Stage() {
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore((String)"Fun2LinkStage", (boolean)true);
            if (recordStore.getNumRecords() > 0) {
                byte[] byArray = recordStore.getRecord(1);
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray, 0, byArray.length));
                this.stage = dataInputStream.readInt();
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(12);
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeInt(this.stage);
                dataOutputStream.flush();
                dataOutputStream.close();
                byte[] byArray = byteArrayOutputStream.toByteArray();
                recordStore.addRecord(byArray, 0, byArray.length);
            }
        }
        catch (Exception exception) {
        }
        finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception exception) {}
            }
        }
    }

    public byte getStage() {
        return (byte)(this.stage + 1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addHighStage(int n) {
        this.stage = n;
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore((String)"Fun2LinkStage", (boolean)true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(12);
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(this.stage);
            dataOutputStream.flush();
            dataOutputStream.close();
            byte[] byArray = byteArrayOutputStream.toByteArray();
            recordStore.setRecord(1, byArray, 0, byArray.length);
        }
        catch (Exception exception) {
        }
        finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception exception) {}
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void initHighStage() {
        this.stage = 0;
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore((String)"Fun2LinkStage", (boolean)true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(12);
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(this.stage);
            dataOutputStream.flush();
            dataOutputStream.close();
            byte[] byArray = byteArrayOutputStream.toByteArray();
            recordStore.setRecord(1, byArray, 0, byArray.length);
        }
        catch (Exception exception) {
        }
        finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception exception) {}
            }
        }
    }
}

