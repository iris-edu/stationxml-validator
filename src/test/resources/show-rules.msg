112      Network:Epoch must encompass all subordinate Station:Epoch [Epoch=startDate-endDate]
101      Network:Code must be assigned a string consisting of 1-2 uppercase characters A-Z and or numeric characters 0-9.
110      Network:startDate must occur before Network:endDate if Network:endDate is available.
111      Station:Epoch cannot be partly concurrent with any other Station:Epoch encompassed in parent Network:Epoch.
210      Station:startDate is required and must occur before Station:endDate if Station:endDate is available.
211      Channel:Epoch cannot be partly concurrent with any other Channel:Epoch encompassed in parent Station:Epoch.
212      Station:Epoch must encompass all subordinate Channel:Epoch [Epoch=startDate-endDate]
201      Station:Code must be assigned a string consisting of 1-5 uppercase characters A-Z and or numeric characters 0-9.
222      Station:Position must be within 1 km of all subordinate Channel:Position.
223      Station:Elevation must be within 1 km of all subordinate Channel:Elevation.
304      Channel:Sensor:Description cannot be null.
305      If Channel:SampleRate is NULL or 0 then Response information should not be included.
310      Channel:startDate is required and must occur before Channel:endDate if Channel:endDate is available.
332      Channel:Azimuth and or Channel:Dip do not correspond within 5 degrees of tolerance to last digit of orthogonal Channel:Code.
301      Channel:Code must be assigned a string consisting of 3 uppercase characters A-Z and or numeric characters 0-9.
302      Channel:locationCode must be unassigned or be assigned a string consisting of 0-2 uppercase characters A-Z and or numeric characters 0-9.
303      Invalid Calibration unit is invalid
420      A Response must contain at least one instance of Response:Stage:Decimation.
421      Stage[Final]:Decimation:InputSampleRate divided by Stage[Final]:Decimation:Factor must equal Channel:SampleRate.
422      Stage[N]:Decimation:InputSampleRate must equal Stage[N-1]:Decimation:InputSampleRate divided by Stage[N-1]:Decimation:Factor.
401      The 'number' attribute of Response::Stage element must start at 1 and be sequential
402      Stage[N]:InputUnits:Name and/or Stage[N]:OutputUnits:Name are not defined in Unit name overview for IRIS StationXML validator.
403      Stage[N]:InputUnits:Name must equal Stage[N-1]:OutputUnits:Name.
404      Stage types FIR|Coefficient|PolesZeros with transfer function type Digital must include Decimation and StageGain elements.
405      Stage of type ResponseList cannot be the only stage available in a response.
410      InstrumentSensitivity:Value cannot be assigned 0 or Null.
411      InstrumentSensitivity:Frequency must be less than Channel:SampleRate/2 [Nyquist Frequency].
412      InstrumentSensitivity:Value must equal the product of all StageGain:Value if all StageGain:Frequency are equal to InstrumentSensitivity:Frequency [Normalization Frequency].
413      StageGain:Value cannot be assigned 0 or Null.
414      If Stage[N] of type PolesZeros contains a Zero where both Real and Imaginary components equal 0 then InstrumentSensitivity:Frequency cannot equal 0 and Stage[N]:StageGain:Frequency cannot equal 0.
415      Response must be defined as Response:InstrumentPolynomial if it contains any Stages defined as ResponseStage:Polynomial