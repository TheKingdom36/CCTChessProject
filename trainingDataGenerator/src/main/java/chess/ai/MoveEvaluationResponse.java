package chess.ai;

public class MoveEvaluationResponse {
    private String theInfo;

    public MoveEvaluationResponse(String theInfo){
        this.theInfo = theInfo;
    }

    public String getTheInfo() {
        return theInfo;
    }

    public void setTheInfo(String theInfo) {
        this.theInfo = theInfo;
    }
}
