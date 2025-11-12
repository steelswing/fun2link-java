import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class Score {
    public int[] values = new int[]{30, 20, 10};

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Score() {
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore((String)"Fun2LinkScore", (boolean)true);
            if (recordStore.getNumRecords() > 0) {
                for (int i = 0; i < 3; ++i) {
                    byte[] byArray = recordStore.getRecord(i + 1);
                    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray, 0, byArray.length));
                    this.values[i] = dataInputStream.readInt();
                }
            } else {
                for (int i = 0; i < 3; ++i) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(12);
                    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    dataOutputStream.writeInt(this.values[i]);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    byte[] byArray = byteArrayOutputStream.toByteArray();
                    recordStore.addRecord(byArray, 0, byArray.length);
                }
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

    public boolean isHighScore(int n) {
        for (int i = 0; i < 3; ++i) {
            if (n < this.values[i]) continue;
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addHighScore(int n) {
        for (int i = 0; i < 3; ++i) {
            if (n < this.values[i]) continue;
            for (int j = 2; j > i; --j) {
                this.values[j] = this.values[j - 1];
            }
            this.values[i] = n;
            RecordStore recordStore = null;
            try {
                recordStore = RecordStore.openRecordStore((String)"Fun2LinkScore", (boolean)true);
                for (int j = 0; j < 3; ++j) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(12);
                    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    dataOutputStream.writeInt(this.values[j]);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    byte[] byArray = byteArrayOutputStream.toByteArray();
                    recordStore.setRecord(j + 1, byArray, 0, byArray.length);
                }
                break;
            }
            catch (Exception exception) {
                break;
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void initHighScore() {
        int n = 10;
        for (int i = 0; i < 3; ++i) {
            for (int j = 2; j > i; --j) {
                this.values[j] = this.values[j - 1];
            }
            this.values[i] = n;
            RecordStore recordStore = null;
            try {
                recordStore = RecordStore.openRecordStore((String)"Fun2LinkScore", (boolean)true);
                for (int j = 0; j < 3; ++j) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(12);
                    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    dataOutputStream.writeInt(this.values[j]);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    byte[] byArray = byteArrayOutputStream.toByteArray();
                    recordStore.setRecord(j + 1, byArray, 0, byArray.length);
                }
                continue;
            }
            catch (Exception exception) {
                continue;
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
}

