package org.microbule.version.decorator;

public class VersionResponse {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String version;
    private final String title;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public VersionResponse(Class<?> serviceInterface) {
        final Package pkg = serviceInterface.getPackage();
        this.version = pkg.getImplementationVersion();
        this.title = pkg.getImplementationTitle();
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }
}
