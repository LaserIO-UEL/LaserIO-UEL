package com.direwolf20.laserio.common.registry;

import com.tterrag.registrate.Registrate;

public class LIOURegistrate extends Registrate {

    public LIOURegistrate(String modid) {
        super(modid);
    }

    public static LIOURegistrate create(String modid) {
      return new LIOURegistrate(modid);
    }

}
