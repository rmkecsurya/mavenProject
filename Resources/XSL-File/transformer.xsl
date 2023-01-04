<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="3.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:msxsl="urn:schemas-microsoft-com:xslt"
	xmlns:utils="urn:my-scripts" exclude-result-prefixes="msxsl utils">
	<xsl:output method="xml" indent="yes"/>
		<!--Flat intermediate BluePrism 1 date:15/05/2020 DD/MM/YYYY-->
	<xsl:output method="xml" indent="yes"/>
	<xsl:param name="GPS_NAME"/>
	<xsl:param name="ImagePath"/>
	<xsl:param name="RepositoryPath"/>
	<xsl:param name="IsDvf"/>
	<xsl:param name="IsValidForm"/>
	<xsl:param name="DefaultData"/>
	<xsl:param name="wndWait"/>
	<xsl:variable name="nodes">PlayBack Info Sentence Standard MSAA</xsl:variable>
	<!--only mention immediate child nodes of Step-->
	<xsl:variable name="StepNodeAttributes">Version ActiveChannelID AdaptorID AliasName ParentID IsStrayStep SecDefHashCode</xsl:variable>
	<xsl:variable name="StepFormNodeAttributes">editable text </xsl:variable>
	<!--only specify step attributes-->
	<xsl:template match="/">
		<Document>
			<Process>
				<xsl:for-each select="WorkFlow/*">
					<xsl:choose>
						<xsl:when test="name()='Step'">
							<xsl:choose>
								<xsl:when test="@IsStrayStep='1'"></xsl:when>
								<xsl:otherwise>
									<xsl:element name="Step">
										<xsl:attribute name="DialogName">
											<xsl:value-of select="PlayBack/AppInfo/@DialogName"/>
										</xsl:attribute>
										<xsl:for-each select="@*">
											<xsl:if test="(name() = 'Version') or (name() = 'ActiveChannelID') or (name() = 'AdaptorID') or (name() = 'AliasName') or (name() = 'SecDefHashCode') or (name() = 'ScreenHashCode')">  
											<!--  <xsl:if test="contains($StepNodeAttributes,name())"> -->
												<xsl:element name="{concat('Step_',name())}">
													<!--Get all attributes of Step element-->
													<xsl:value-of select="."/>
												</xsl:element>
											</xsl:if>
										</xsl:for-each>
										<xsl:for-each select="node()">
											<!--forEach immediate child element of Step element-->
											<xsl:variable name="parentName">
												<xsl:value-of select="name()"/>
											</xsl:variable>
											<!--<xsl:if test="($parentName='PlayBack') or ($parentName='Sentence')">  -->
											<xsl:if test="contains($nodes,$parentName)">
												<xsl:for-each select="@*">
													<xsl:element name="{concat($parentName,'_',name())}">
														<xsl:value-of select="."/>
													</xsl:element>
												</xsl:for-each>
												<xsl:call-template name="buildElementTemplate">
													<xsl:with-param name="parentName">
														<xsl:value-of select="$parentName"/>
													</xsl:with-param>
												</xsl:call-template>
											</xsl:if>
										</xsl:for-each>
									</xsl:element>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:for-each>
			</Process>
			<Meta>
				<GpsFile>
					<xsl:value-of select="$GPS_NAME"/>
				</GpsFile>
				<ExePath>
					<xsl:value-of select="concat(WorkFlow/Header/TAPS/TAP/@Path,'\',WorkFlow/Header/TAPS/TAP/@EXEName)"/>
				</ExePath>
				<ImagePath>
					<xsl:value-of select="$ImagePath"/>
				</ImagePath>
				<RepositoryPath>
					<xsl:value-of select="$RepositoryPath"/>
				</RepositoryPath>
				<IsDvf>
					<xsl:value-of select="$IsDvf"/>
				</IsDvf>
				<DefaultData>
					<xsl:value-of select="$DefaultData"/>
				</DefaultData>
				<wndWait>
					<xsl:value-of select="$wndWait"/>
				</wndWait>
			</Meta>
		</Document>
	</xsl:template>
	<xsl:template name="buildElementTemplate">
		<!--Recursive template-->
		<xsl:param name="parentName"/>
		<xsl:if test="*">
			<xsl:for-each select="node()">
			
				<xsl:variable name="elementName">
					<xsl:value-of select="name()"/>
				</xsl:variable>
				<xsl:if test="($elementName != 'TimeStamp' and $elementName != 'AdditionalInfo' and $elementName != 'ClickPoints' and $elementName != 'Point' and $elementName != 'Location' and $elementName != 'ClientLocation' and $elementName != 'Model')">
				<xsl:variable name="elementNameFinalWithoutAttribute">
					<xsl:value-of select="concat($parentName,'__',$elementName)"/>
				</xsl:variable>
				<xsl:for-each select="@*">
				<xsl:if test="(name() != 'ControlID') and (name() != 'EventTime') and (name() != 'Handle') and (name() != 'ParentHandle') and (name() != 'ProcessID') and (name() != 'ControlImage') and (name() != 'GUID') and (name() != 'PROCESSID') 
				and (name() != 'LPARAM') and (name() != 'ControlMouseCL') and (name() != 'RoleID')">
					<xsl:element name="{concat($elementNameFinalWithoutAttribute,'_',name())}">
						<xsl:value-of select="."/>
					</xsl:element>
					</xsl:if>
				</xsl:for-each>
				<xsl:if test="*">
					<xsl:call-template name="buildElementTemplate">
						<xsl:with-param name="parentName">
							<xsl:value-of select="$elementNameFinalWithoutAttribute"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
