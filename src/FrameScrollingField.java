/**
 * <b>FrameScrollingfield</b> - A Field with neighbors.
 * @author Yama H
 * @version 0.7a
 */
public class FrameScrollingField extends Field
{
    /**
     * Field to reach when traveling north of this Field.
     */
    private FrameScrollingField northField = null;
    /**
     * Field to reach when traveling east of this Field.
     */
    private FrameScrollingField eastField = null;
    /**
     * Field to reach when traveling south of this Field.
     */
    private FrameScrollingField southField = null;
    /**
     * Field to reach when traveling west of this Field.
     */
    private FrameScrollingField westField = null;
    /**
     * Constructor for objects of type FrameScrollingField.
     * @param layer1 name of first layer image
     * @param layer2 name of second layer image
     */
    public FrameScrollingField(String layer1, String layer2)
    {
        super(layer1, layer2);
    }
    public void load()
    {
    	if(bitmap == null)
    		allocate();
    	if(northField != null)
    	{
    		northField.allocate();
    	}
    	if(southField != null)
    	{
    		southField.allocate();
    	}
    	if(westField != null)
    	{
    		westField.allocate();
    	}
    	if(eastField != null)
    	{
    		eastField.allocate();
    	}
        startupScript.run(this);
    }
    public void close(FrameScrollingField newField)
    {
    	if(northField != null && northField != newField)
    	{
    		northField.free();
    	}
    	if(southField != null && southField != newField)
    	{
    		southField.free();
    	}
    	if(westField != null && westField != newField)
    	{
    		westField.free();
    	}
    	if(eastField != null && eastField != newField)
    	{
    		eastField.free();
    	}
    }
    protected void setNorthField(FrameScrollingField northField)
    {
        this.northField = northField;
    }
    protected void setEastField(FrameScrollingField eastField)
    {
        this.eastField = eastField;
    }
    protected void setSouthField(FrameScrollingField southField)
    {
        this.southField = southField;
    }
    protected void setWestField(FrameScrollingField westField)
    {
        this.westField = westField;
    }
    protected FrameScrollingField getNorthField()
    {
        return northField;
    }
    protected FrameScrollingField getEastField()
    {
        return eastField;
    }
    protected FrameScrollingField getSouthField()
    {
        return southField;
    }
    protected FrameScrollingField getWestField()
    {
        return westField;
    }
}