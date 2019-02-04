package com.appgrouplab.icd11.content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jose_Segura on 15/12/16.
 */

public class MenuCategoria {

    public static List<contentItem> ITEMS = new ArrayList<contentItem>();

    static {
        addItem(new contentItem("1", "[1A0-1G8]","Certain infectious or parasitic diseases"));
        addItem(new contentItem("2", "[2A0-2F9]","Neoplasms"));
        addItem(new contentItem("3", "[3A0-3B8]","Diseases of the blood or blood-forming organs"));
        addItem(new contentItem("4", "[4A0-4B2]","Diseases of the immune system"));
        addItem(new contentItem("5", "[5A0-5D4]","Endocrine, nutritional or metabolic diseases"));
        addItem(new contentItem("6", "[6A0-6E6]","Mental, behavioural or neurodevelopmental disorders"));
        addItem(new contentItem("7", "[7A0-7B0]","Sleep-wake disorders"));
        addItem(new contentItem("8", "[8A0-8E6]","Diseases of the nervous system"));
        addItem(new contentItem("9", "[9A0-9D9]","Diseases of the visual system"));
        addItem(new contentItem("10", "[AA0-AB9]","Diseases of the ear or mastoid process"));
        addItem(new contentItem("11", "[BA0-BE1]","Diseases of the circulatory system"));
        addItem(new contentItem("12", "[CA0-CB6]","Diseases of the respiratory system"));
        addItem(new contentItem("13", "[DA0-DE1]","Diseases of the digestive system"));
        addItem(new contentItem("14", "[EA0-EL7]","Diseases of the skin"));
        addItem(new contentItem("15", "[FA0-FB8]","Diseases of the musculoskeletal system or connective tissue"));
        addItem(new contentItem("16", "[GA0-GC7]","Diseases of the genitourinary system"));
        addItem(new contentItem("17", "[HA0-HA6]","Conditions related to sexual health"));
        addItem(new contentItem("18", "[JA0-JB6]","Pregnancy, childbirth or the puerperium"));
        addItem(new contentItem("19", "[KA0-KD3]","Developmental anomalies"));
        addItem(new contentItem("20", "[LA0-LD5]","Certain conditions originating in the perinatal period"));
        addItem(new contentItem("21", "[MA0-MH1]","Symptoms, signs or clinical findings, not elsewhere classified"));
        addItem(new contentItem("22", "[NA0-NF0]","Injury, poisoning or certain other consequences of external causes"));
        addItem(new contentItem("23", "[PA0-PL0]","External causes of morbidity or mortality"));
        addItem(new contentItem("24", "[QA0-QF2]","Factors influencing health status or contact with health services"));
        addItem(new contentItem("25", "[RA0-RA2]","Codes for special purposes"));
        addItem(new contentItem("26", "[SA0-SH7]","Traditional Medicine conditions - Module I"));
        addItem(new contentItem("V", "[VA0-VC5]","Supplementary section for functioning assessment"));
        addItem(new contentItem("X", "","Extension Codes"));

    }

    private static void addItem(contentItem item) {
        ITEMS.add(item);
    }

    public static class contentItem {
        public String capitulo;
        public String codigos;
        public String titulo;

        public contentItem(String capitulo, String codigos, String titulo) {
            this.capitulo = capitulo;
            this.codigos = codigos;
            this.titulo = titulo;
        }

        @Override
        public String toString() {
            return titulo;
        }
    }

}
