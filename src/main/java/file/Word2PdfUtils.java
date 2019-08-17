package file;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


public class Word2PdfUtils {
    private static final Integer WORD_TO_PDF_OPERAND = 17;

    public void doc2pdf(String srcFilePath, String pdfFilePath) throws Exception {
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Object[] obj = new Object[]{
                    srcFilePath,
                    new Variant(false),
                    new Variant(false),//是否只读
                    new Variant(false),
                    new Variant("pwd")
            };
            doc = Dispatch.invoke(docs, "Open", Dispatch.Method, obj, new int[1]).toDispatch();
//          Dispatch.put(doc, "Compatibility", false);  //兼容性检查,为特定值false不正确
            Dispatch.put(doc, "RemovePersonalInformation", false);
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFilePath, WORD_TO_PDF_OPERAND); // word保存为pdf格式宏，值为17

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (doc != null) {
                Dispatch.call(doc, "Close", false);
            }
            if (app != null) {
                app.invoke("Quit", 0);
            }
            ComThread.Release();
        }
    }

    public void TestConvert() throws Exception {
        String path = "./etc/doc";
        File parent = new File(path);
        if (parent.listFiles() != null) {
            for (File file : parent.listFiles()) {
                if (file.getName().toLowerCase().endsWith(".doc") ||file.getName().toLowerCase().endsWith(".docx")) {
                    new Word2PdfUtils().doc2pdf(path+file.getName(),path+file.getName()+".pdf");
                }
            }
        }
    }

}
