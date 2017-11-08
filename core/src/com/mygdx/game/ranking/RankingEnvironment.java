package com.mygdx.game.ranking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.item.Config;
import com.mygdx.game.item.ResultData;
import com.mygdx.game.main.DatabaseOperator;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.item.Config.skin;

/**
 * Created by ntani on 2017/11/08.
 */

class RankingEnvironment {
    private String TAG = RankingEnvironment.class.getSimpleName();
    List<ResultData> resultList;
    List<ResultData> showList;
    String SCREEN_MODE;
    String viewMode;
    String crntVMode;

    RankingEnvironment(DatabaseOperator dbo) {
        Gdx.app.log(TAG, "constractor");
        SCREEN_MODE = Config.NO_SLCT;
        crntVMode = viewMode = Config.PLAY_LV1;
        resultList = dbo.read();
    }

    // テーブルセット
    Table setTable() {
        showList = getByKey(viewMode);
        Table container = new Table();
        Table table = new Table(skin);
        ScrollPane pane = new ScrollPane(table);
        container.add(pane).width(Config.SCRN_WIDTH * 0.65f).height(Config.SCRN_HEIGHT * 0.8f);
        for(ResultData rd : showList) {
            Gdx.app.log(TAG, rd.toString());
            container.row();
            Label name = new Label(rd.name, skin);
            Label time = new Label(rd.generateSec(), skin);
            name.setWrap(true);
            time.setWrap(true);
            container.add(name);
            container.add(time);
        }
        return container;
    }

    // キーによるリストの絞り込み
    private List<ResultData> getByKey(String key) {
        List<ResultData> retList = new ArrayList<ResultData>();
        for(ResultData rd : resultList) {
            if(key.equals(rd.mode)) retList.add(rd);
        }
        return retList;
    }

    // ランキングモードが変更されたか
    boolean isRankingChangeed() {
        // 変更なし
        if(viewMode.equals(crntVMode)) {
            return false;
        }
        // 変更あり
        else {
            Gdx.app.log(TAG, "change ranking mode to " + viewMode);
            crntVMode = viewMode;
            return true;
        }
    }

    // ランキングモード表示用ラベル
    Label getModeLabel(String mode){
        float width = 500f;
        float height = 100f;
        Label label = new Label(mode + " mode Ranking", skin);
        label.setFontScale(2);
        label.setSize(width, height);
        label.setPosition(Config.SCRN_WIDTH_CTR - width / 2, Config.SCRN_HEIGHT - height);
        label.setColor(0,0,0,1);
        label.setAlignment(Align.center);
        return label;
    }


    // タイトルへ戻るボタン
    TextButton getTitleButton(String label) {
        TextButton txtBtn = new TextButton(label, skin);
        txtBtn.setSize(100f, 50f);
        txtBtn.setPosition(0f, 0f);
        setBtnListener(txtBtn);
        return txtBtn;
    }

    // easyランキングモード
    TextButton getLv1RankingButton() {
        TextButton txtBtn = new TextButton(Config.PLAY_LV1, skin);
        txtBtn.setSize(100f, 50f);
        txtBtn.setPosition(Config.SCRN_WIDTH - 100f, 60f);
        setChgModeListener(txtBtn);
        return txtBtn;
    }

    // hardランキングモード
    TextButton getLv2RankingButton() {
        TextButton txtBtn = new TextButton(Config.PLAY_LV2, skin);
        txtBtn.setSize(100f, 50f);
        txtBtn.setPosition(Config.SCRN_WIDTH - 100f, 0f);
        setChgModeListener(txtBtn);
        return txtBtn;
    }

    // ボタン名をそのままSCREEN_MODEへ渡すリスナー
    private void setBtnListener(final TextButton txtBtn) {
        txtBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                SCREEN_MODE = txtBtn.getText().toString();
                return true;
            }
        });
    }

    // ランキング表示モードを変更するリスナー
    private void setChgModeListener(final TextButton txtBtn) {
        txtBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                viewMode = txtBtn.getText().toString();
                return true;
            }
        });
    }

}
