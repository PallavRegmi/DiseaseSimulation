package Disease;
/**
 * Bundles together all the settings and configurations for managing groups of Agents.
 *
 */

public class AgentInfoLayout {

    private LayoutTypeEnum layoutTypeEnum;
    private int cols;
    private int rws;
    private int agentNumber;
    private int initSick = 1;
    private int initImmune = 0;

    public AgentInfoLayout(String agentLayout) {
        switch (agentLayout) {
            case "grid" -> layoutTypeEnum = LayoutTypeEnum.GRID;
            case "randomgrid" -> { layoutTypeEnum = LayoutTypeEnum.RANDOMGRID; }
            case "random" -> { layoutTypeEnum = LayoutTypeEnum.RANDOM; }
            default -> { layoutTypeEnum = LayoutTypeEnum.GRID; }
        }
    }


    public AgentInfoLayout() {
        layoutTypeEnum = LayoutTypeEnum.GRID;
    }

    public void setLayoutType(String agentLayout) {
        switch (agentLayout) {
            case "grid" -> { layoutTypeEnum = LayoutTypeEnum.GRID; }
            case "randomgrid" -> { layoutTypeEnum = LayoutTypeEnum.RANDOMGRID; }
            case "random" -> { layoutTypeEnum = LayoutTypeEnum.RANDOM; }
            default -> { layoutTypeEnum = LayoutTypeEnum.GRID; }
        }
    }

    public void setCols(int width) {
        cols = width;
    }

    public void setRws(int height) {
        rws = height;
    }

    public void setAgentNumber(int n) {
        agentNumber = n;
    }

    public void setInitSick(int n) { initSick = n;}

    public void setInitImmune(int n) {
        initImmune = n;}

    public LayoutTypeEnum getLayoutType() {
        return this.layoutTypeEnum;
    }

    public int getCols() {
        return cols;
    }

    public int getRws() {
        return rws;
    }

    public int getAgentNumber() {
        return agentNumber;
    }

    public int getInitSick() {return initSick;}

    public int getInitImmune() {return initImmune;}

}