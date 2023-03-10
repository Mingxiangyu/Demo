package cn.trans.eunms;

/**
 * 翻译支持语种
 */
public enum TransLangEnum {

    AUTO("auto","自动检测", false),
    EN("en","英语", false),
    JA("ja","日语", false),
    KOR("kor","韩语", false),
    FRA("fra","法语", false),
    SPA("spa","西班牙语", false),
    TH("th","泰语", false),
    ARA("ara","阿拉伯语", false),
    RU("ru","俄语", false),
    PT("pt","葡萄牙语", false),
    DE("de","德语", false),
    IT("it","意大利语", false),
    EL("el","希腊语", false),
    NL("nl","荷兰语", false),
    BUL("bul","保加利亚语", false),
    EST("est","爱沙尼亚语", false),
    DAN("dan","丹麦语", false),
    FIN("fin","芬兰语", false),
    CS("cs","捷克语", false),
    ROM("rom","罗马尼亚语", false),
    SLO("slo","斯洛文尼亚语", false),
    SWE("swe","瑞典语", false),
    HU("hu","匈牙利语", false),
    CHT("cht","中文(繁体)", false),
    VIE("vie","越南语", false),
    YUE("yue","中文(粤语)", false),
    WYW("wyw","中文(文言文)", false),
    ZH("zh","中文(简体)", false),

    ALB("alb","阿尔巴尼亚语", true),
    AMH("amh","阿姆哈拉语", true),
    ARM("arm","亚美尼亚语", true),
    ASM("asm","阿萨姆语", true),
    AST("ast","阿斯图里亚斯语", true),
    AZE("aze","阿塞拜疆语", true),
    BAQ("baq","巴斯克语", true),
    BEL("bel","白俄罗斯语", true),
    BEN("ben","孟加拉语", true),
    BER("ber","柏柏尔语", true),
    BOS("bos","波斯尼亚语", true),
    BRE("bre","布列塔尼语", true),
    BUR("bur","缅甸语", true),
    CAT("cat","加泰罗尼亚语", true),
    EPO("epo","世界语", true),
    FIL("fil","菲律宾语", true),
    FRY("fry","西弗里斯语", true),
    GEO("geo","格鲁吉亚语", true),
    GLE("gle","爱尔兰语", true),
    GLG("glg","加利西亚语", true),
    GUJ("guj","古吉拉特语", true),
    HEB("heb","希伯来语", true),
    HI("hi","印地语", true),
    HKM("hkm","高棉语", true),
    HRV("hrv","克罗地亚语", true),
    ICE("ice","冰岛语", true),
    ID("id","印尼语", true),
    KAB("kab","卡拜尔语", true),
    KAN("kan","卡纳达语", true),
    KIN("kin","卢旺达语", true),
    KUR("kur","库尔德语", true),
    LAT("lat","拉丁语", true),
    LAV("lav","拉脱维亚语", true),
    LIT("lit","立陶宛语", true),
    LOG("log","低地德语", true),
    MAC("mac","马其顿语", true),
    MAI("mai","迈蒂利语", true),
    MAL("mal","马拉雅拉姆语", true),
    MAY("may","马来语", true),
    MG("mg","马拉加斯语", true),
    MLT("mlt","马耳他语", true),
    NNO("nno","新挪威语", true),
    NOB("nob","书面挪威语", true),
    OCI("oci","奥克语", true),
    ORI("ori","奥里亚语", true),
    PAN("pan","旁遮普语", true),
    PER("per","波斯语", true),
    PL("pl","波兰语", true),
    SIN("sin","僧伽罗语", true),
    SK("sk","斯洛伐克语", true),
    SOM("som","索马里语", true),
    SRP("srp","塞尔维亚语", true),
    SWA("swa","斯瓦希里语", true),
    TAM("tam","泰米尔语", true),
    TAT("tat","鞑靼语", true),
    TEL("tel","泰卢固语", true),
    TGK("tgk","塔吉克语", true),
    TGL("tgl","他加禄语", true),
    TR("tr","土耳其语", true),
    UKR("ukr","乌克兰语", true),
    URD("urd","乌尔都语", true),
    WEL("wel","威尔士语", true),
    WLN("wln","瓦隆语", true),
    XHO("xho","科萨语", true),
    ;

    private final String name;
    private final String key;
    private final Boolean vip;

    TransLangEnum(String key,String name, Boolean vip) {
        this.key = key;
        this.name = name;
        this.vip = vip;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Boolean getVip() {
        return vip;
    }

    public static TransLangEnum getByKey(String key) {
        for(TransLangEnum item : values()) {
            if(item.getKey().equals(key)) {
                return item;
            }
        }
        return null;
    }
}
