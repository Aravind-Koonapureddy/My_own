package com.snaplogic.snaps.Dependency;


import com.snaplogic.common.properties.SnapProperty;
import com.snaplogic.common.properties.builders.PropertyBuilder;
import com.snaplogic.snap.api.*;
import com.snaplogic.snap.api.capabilities.*;


@General(title = "LookUp", purpose = "read given csv file",
        author = "Snaplogic", docLink = "http://www.docs.com/mysnap")
@Inputs(min = 0, max = 1, accepts = {ViewType.DOCUMENT})
@Outputs(min = 1, max = 1, offers = {ViewType.DOCUMENT})
@Errors(min = 1, max = 1, offers = {ViewType.DOCUMENT})
@Version(snap = 1)
@Category(snap = SnapCategory.READ)
public class MessagesCheck extends SimpleSnap {


    String ArtifactID = "ArtifactID";
    String Path = "Path";
    private ExpressionProperty ArtifactIDExp;
    private ExpressionProperty PathExp;

    @Override
    public void defineProperties(PropertyBuilder propertyBuilder) {

        propertyBuilder.describe(ArtifactID, "ArtifactID","ArtifactID of the dependency")
                .expression(SnapProperty.DecoratorType.ACCEPTS_SCHEMA)
                .required()
                .add();
        propertyBuilder.describe(Path,"Path","Path of the dependency")
                .expression()
                .required()
                .add();
    }

    @Override
    public void configure(PropertyValues propertyValues)  {
        ArtifactIDExp = propertyValues.getAsExpression(ArtifactID);
        PathExp = propertyValues.getAsExpression(Path);

    }

    @Override
    protected void process(Document document, String s) {
        String ArtifactID = ArtifactIDExp.eval(document);
        String path = PathExp.eval(document);
    }
}
