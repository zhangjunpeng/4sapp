package dao.test4s;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class ExampleGenerator {
    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.test4s.gdb");
        addGameInfo(schema);
        addInformation(schema);
        new DaoGenerator().generateAll(schema, "E:\\work\\MyApp\\app\\src\\main\\java");
    }


    private static void addGameInfo(Schema schema) {
        Entity gameInfo = schema.addEntity("GameInfo");
        Entity investment=schema.addEntity("Investment");
        Entity outSource=schema.addEntity("OutSource");
        Entity ip=schema.addEntity("IP");
        Entity cp=schema.addEntity("CP");
        Entity distribution=schema.addEntity("Distribution");

        //游戏详情
        gameInfo.addIdProperty().primaryKey();
        gameInfo.addStringProperty("name");
        Property gameid=gameInfo.addLongProperty("gameid").getProperty();
        gameInfo.addStringProperty("tips");
        gameInfo.addStringProperty("playerSource");
        gameInfo.addLongProperty("upTime");
        gameInfo.addStringProperty("others");
        gameInfo.addStringProperty("introuduction");
        gameInfo.addStringProperty("imageUrl");
        gameInfo.addStringProperty("upDescription");
        gameInfo.addStringProperty("companyGameId");
        gameInfo.addStringProperty("comment");



        //Investment投资公司
        investment.addIdProperty().primaryKey();
        investment.addStringProperty("name");
        investment.addStringProperty("introuduction");
        investment.addStringProperty("location");
        investment.addStringProperty("scale");
        investment.addStringProperty("webSite");
        investment.addStringProperty("telePhone");
        investment.addStringProperty("address");

        ToMany gameToInvestment = investment.addToMany(gameInfo,gameid);

        gameToInvestment.setName("cases");



        //外包公司
        outSource.addIdProperty().primaryKey();
        outSource.addStringProperty("name");
        outSource.addStringProperty("type");
        outSource.addStringProperty("introuduction");
        outSource.addStringProperty("location");
        outSource.addStringProperty("scale");
        outSource.addStringProperty("webSite");
        outSource.addStringProperty("telePhone");
        outSource.addStringProperty("address");
        ToMany outSourceToGame=  outSource.addToMany(gameInfo,gameid);
        outSourceToGame.setName("cases");


        //ip
        ip.addIdProperty().primaryKey();
        ip.addStringProperty("ipId");
        ip.addStringProperty("name");
        ip.addStringProperty("type");
        ip.addStringProperty("style");
        ip.addStringProperty("range");
        ip.addStringProperty("introuduction");
        ip.addStringProperty("location");
        ip.addStringProperty("scale");
        ip.addStringProperty("webSite");
        ip.addStringProperty("telePhone");
        ip.addStringProperty("address");
        ip.addStringProperty("otherIp");

        //cp游戏开发
        cp.addIdProperty().primaryKey();
        cp.addStringProperty("name");
        cp.addStringProperty("introuduction");
        cp.addStringProperty("location");
        cp.addStringProperty("scale");
        cp.addStringProperty("webSite");
        cp.addStringProperty("telePhone");
        cp.addStringProperty("address");

        ToMany cpToGame = cp.addToMany(gameInfo,gameid);

        gameToInvestment.setName("cases");


        //distribution
        distribution.addIdProperty().primaryKey();
        distribution.addStringProperty("name");
        distribution.addStringProperty("introuduction");
        distribution.addStringProperty("location");
        distribution.addStringProperty("scale");
        distribution.addStringProperty("webSite");
        distribution.addStringProperty("telePhone");
        distribution.addStringProperty("address");

        ToMany distributionToGame = investment.addToMany(gameInfo,gameid);

        gameToInvestment.setName("cases");


    }

    private static void addInformation(Schema schema) {
        Entity information = schema.addEntity("Information");

        information.addIdProperty().primaryKey();
        information.addStringProperty("title").notNull();
        information.addStringProperty("time").notNull();
        information.addIntProperty("looknum");
        information.addIntProperty("comment_num");
        information.addStringProperty("context");


    }
}
